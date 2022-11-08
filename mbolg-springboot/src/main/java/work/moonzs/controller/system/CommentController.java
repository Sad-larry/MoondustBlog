package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
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
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "评论列表")
    @GetMapping("/list")
    public ResponseResult listComment(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(commentService.listComment(pageNum, pageSize));
    }

    /**
     * 删除评论
     *
     * @param commentIds 评论id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据评论id进行批量删除操作")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteComment(@PathVariable(value = "ids") Long[] commentIds) {
        commentService.deleteComment(commentIds);
        return ResponseResult.success();
    }
}
