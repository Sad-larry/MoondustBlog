package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.CommentVo;
import work.moonzs.domain.vo.PageVo;

/**
 * 评论表(Comment)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:23
 */
public interface CommentService extends IService<Comment> {

    /**
     * 评论列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    PageVo<CommentVo> listComments(Integer pageNum, Integer pageSize);
}

