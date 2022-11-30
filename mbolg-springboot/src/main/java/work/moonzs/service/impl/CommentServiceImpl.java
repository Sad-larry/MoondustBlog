package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysCommentVO;
import work.moonzs.domain.vo.web.CommentVO;
import work.moonzs.mapper.CommentMapper;
import work.moonzs.service.CommentService;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:10
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

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
    public List<CommentVO> listArticleComment(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        List<Comment> list = list(queryWrapper);
        // 如果查询到该文章的评论为空，则直接返回空
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // TODO 明天再做
        return null;
    }
}

