package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.sys.SysCommentVo;
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
    public PageVo<SysCommentVo> listComment(Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        List<SysCommentVo> commentListVos = baseMapper.listCommentPage(page);
        return new PageVo<>(commentListVos, page);
    }

    @Override
    public boolean deleteComment(Long[] commentIds) {
        return removeBatchByIds(List.of(commentIds));
    }
}

