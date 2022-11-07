package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.ArticleService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebArticleC")
@RequestMapping(value = "/web/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 获取文章列表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult listWebArticle(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(articleService.listWebArticle(pageNum, pageSize));
    }
}
