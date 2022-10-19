package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.CommentDTO;
import work.moonzs.domain.entity.Comment;
import work.moonzs.service.CommentService;

/**
 * @author Moondust月尘
 */
@RestController("SystemCommentC")
@RequestMapping("/system/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 评论列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listComments(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return commentService.listComments(pageNum, pageSize);
    }

    /**
     * 更新评论
     * 突然想到，一般更新操作，是不会去改变他的状态的的吧，只有单独删除操作，才会改变他的状态
     *
     * @param commentDTO 评论dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult<?> updateComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = BeanCopyUtils.copyBean(commentDTO, Comment.class);
        commentService.updateById(comment);
        return ResponseResult.success();
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteComment(@PathVariable(value = "id") Long commentId) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setStatus(StatusConstants.DISABLE);
        commentService.updateById(comment);
        return ResponseResult.success();
    }
}
