package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Comment;
import work.moonzs.domain.vo.CommentVo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysCommentVO;

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
     * @return {@link PageVO}<{@link CommentVo}>
     */
    PageVO<SysCommentVO> listComment(Integer pageNum, Integer pageSize);


    /**
     * 批量删除评论
     *
     * @param commentIds 评论id
     * @return boolean
     */
    @Transactional
    boolean deleteComment(Long[] commentIds);
}

