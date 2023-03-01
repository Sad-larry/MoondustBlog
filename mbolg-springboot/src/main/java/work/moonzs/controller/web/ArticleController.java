package work.moonzs.controller.web;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.ArticleService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.TagService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebArticleC")
@RequestMapping(value = "/web/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;

    /**
     * 获取文章列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取文章列表")
    @GetMapping("/list")
    public ResponseResult listWebArticle(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(articleService.listWebArticle(pageNum, pageSize, null, null));
    }

    /**
     * 通过查询指定id字段获取文章列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取指定文章列表")
    @GetMapping("/condition")
    public ResponseResult listArticleByQuaryId(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        String name = null;
        if (ObjUtil.isNotNull(categoryId)) {
            name = categoryService.getById(categoryId).getName();
        } else if (ObjUtil.isNotNull(tagId)) {
            name = tagService.getById(tagId).getName();
        }
        return ResponseResult.success(articleService.listWebArticle(pageNum, pageSize, categoryId, tagId))
                .put("name", name);
    }

    /**
     * 文章详情
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "文章详情")
    @GetMapping(value = "/info")
    public ResponseResult getArticleInfo(Long id) {
        return ResponseResult.success(articleService.getArticleInfo(id));
    }

    /**
     * 查询归档
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "查询归档")
    @GetMapping("/archive")
    public ResponseResult getArchives(@RequestParam(defaultValue = "1", required = false) Integer pageNum,
                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.successPageVO(articleService.getArchives(pageNum, pageSize));
    }

    /**
     * 搜索文章
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "查询归档")
    @GetMapping("/search")
    public ResponseResult searchArticles(@RequestParam String keywords) {
        return ResponseResult.successPageVO(articleService.searchArticles(keywords));
    }
}
