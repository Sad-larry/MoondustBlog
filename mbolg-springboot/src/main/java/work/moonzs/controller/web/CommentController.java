package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.annotation.WebOperationLogger;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.CommentDTO;
import work.moonzs.domain.entity.Comment;
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
    @GetMapping("/list")
    public ResponseResult listArticleComment(@RequestParam Long articleId) {
        return ResponseResult.successPageVO(commentService.listArticleComment(articleId));
    }

    /**
     * 发表评论
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "发表评论")
    @WebOperationLogger(value = "评论模块-发表评论", type = "添加", desc = "用户评论")
    @PostMapping("/send")
    public ResponseResult sendComment(@Validated @RequestBody CommentDTO commentDTO) {
        return ResponseResult.success(commentService.sendArticleComment(BeanCopyUtil.copyBean(commentDTO, Comment.class)));
    }
}
