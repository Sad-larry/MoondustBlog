package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysCommentVO;
import work.moonzs.domain.vo.web.CommentVO;
import work.moonzs.domain.vo.web.ReplyVO;
import work.moonzs.domain.vo.web.UserInfoVO;
import work.moonzs.mapper.CommentMapper;
import work.moonzs.service.CommentService;
import work.moonzs.service.UserService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:10
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public PageVO<SysCommentVO> listComment(Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        List<SysCommentVO> commentListVos = baseMapper.listCommentPage(page);
        return new PageVO<>(commentListVos, page.getTotal());
    }

    @Override
    public boolean deleteComment(Long[] commentIds) {
        return removeBatchByIds(List.of(commentIds));
    }

    @Override
    public PageVO<CommentVO> listArticleComment(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        List<Comment> comments = list(queryWrapper);
        // 如果查询到该文章的评论为空，则直接返回空
        if (CollUtil.isEmpty(comments)) {
            return new PageVO<>();
        }
        // 从列表中找到用户Id，好查询该用户的信息
        List<Long> userIds = comments.stream().map(Comment::getUserId).distinct().toList();
        List<UserInfoVO> webUsers = userService.getWebUserByIds(userIds);
        // 为每个用户添加一一对应关系
        HashMap<Long, UserInfoVO> userMap = new HashMap<>();
        webUsers.forEach(userInfoVO -> userMap.putIfAbsent(userInfoVO.getId(), userInfoVO));
        // 最终评论集合
        HashMap<Long, CommentVO> commentMap = new HashMap<>();
        // 因为会有丢失数据，所以不能返回 comments.size()
        AtomicReference<Long> commentCount = new AtomicReference<>(0L);
        // 先设置一级评论，将所有的一级评论放入评论图里
        for (int i = comments.size() - 1; i >= 0; i--) {
            // 如果当前评论为文章的一级评论
            if (ObjUtil.isNull(comments.get(i).getParentId())) {
                CommentVO commentVO = BeanCopyUtil.copyBean(comments.get(i), CommentVO.class);
                // 为评论设置用户信息
                Long userId = commentVO.getUserId();
                if (userMap.containsKey(userId)) {
                    UserInfoVO userInfoVO = userMap.get(userId);
                    commentVO.setAvatar(userInfoVO.getAvatar());
                    commentVO.setNickname(userInfoVO.getNickname());
                    // 设置回复评论
                    commentVO.setReplyVOList(new LinkedList<>());
                    // 添加一级评论
                    commentMap.put(comments.get(i).getId(), commentVO);
                    // 评论数+1
                    commentCount.getAndSet(commentCount.get() + 1);
                }
                // 添加成功后删除集合中的当前元素，用户被注销，该评论不存在也可以删除
                comments.remove(i);
            }
        }
        // 将二级评论添加到一级评论中
        // TODO 目前只支持对一级评论的回复，若对二级评论回复则会丢失数据
        comments.forEach(comment -> {
            CommentVO commentVO = commentMap.get(comment.getParentId());
            if (null != commentVO) {
                LinkedList<ReplyVO> replyVOList = (LinkedList<ReplyVO>) commentVO.getReplyVOList();
                // 回复一级评论的评论
                ReplyVO replyVO = BeanCopyUtil.copyBean(comment, ReplyVO.class);
                // 评论用户id
                Long userId = replyVO.getUserId();
                // 回复用户Id
                Long replyUserId = replyVO.getReplyUserId();
                if (userMap.containsKey(userId) && userMap.containsKey(replyUserId)) {
                    // 为评论设置用户信息
                    UserInfoVO userInfoVO = userMap.get(userId);
                    replyVO.setAvatar(userInfoVO.getAvatar());
                    replyVO.setNickname(userInfoVO.getNickname());
                    // 设置被回复的用户信息
                    UserInfoVO replyUserInfoVO = userMap.get(replyUserId);
                    replyVO.setReplyAvatar(replyUserInfoVO.getAvatar());
                    replyVO.setReplyNickname(replyUserInfoVO.getNickname());
                    // 回复列表添加元素
                    replyVOList.addFirst(replyVO);
                    commentVO.setReplyCount(replyVOList.size());
                    commentVO.setReplyVOList(replyVOList);
                    // 重新写覆盖入评论图
                    commentMap.put(comment.getParentId(), commentVO);
                    // 评论数+1
                    commentCount.getAndSet(commentCount.get() + 1);
                }
            }
        });
        // 返回时也按照时间倒序返回
        return new PageVO<>(commentMap.values().stream().sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).toList(), commentCount.get());
    }

    @Override
    public Long sendArticleComment(Comment comment) {
        baseMapper.insert(comment);
        return comment.getId();
    }
}

