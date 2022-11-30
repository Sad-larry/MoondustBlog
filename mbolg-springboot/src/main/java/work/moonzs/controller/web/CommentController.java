package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.CommentService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebCommentC")
@RequestMapping(value = "/web/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 获取文章评论列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取文章评论列表")
    @GetMapping("/comments")
    public ResponseResult listArticleComment(Long articleId) {
        return ResponseResult.success(commentService.listArticleComment(articleId));
    }
}
