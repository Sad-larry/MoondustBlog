package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.sys.SysCommentVo;

import java.util.List;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:32:53
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 页面评论列表
     *
     * @param page 页面
     * @return {@link List}<{@link SysCommentVo}>
     */
    List<SysCommentVo> listCommentPage(Page<Comment> page);
}

