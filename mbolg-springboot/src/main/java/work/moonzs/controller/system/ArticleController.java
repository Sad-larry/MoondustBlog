package work.moonzs.controller.system;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.service.ArticleService;
import work.moonzs.service.ArticleTagService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.TagService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Moondust月尘
 */
@RestController("SystemArtileC")
@RequestMapping("/system/article")
@RequiredArgsConstructor
public class ArticleController {
    // 建议用构造器注入(需要写final?)而不是使用`@Autowired`注解，@RequiredArgsConstructor自动帮忙写构造器...
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    /**
     * 发表文章
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "发表文章")
    @PostMapping
    public ResponseResult publishArticle(@Validated(value = VG.Insert.class) @RequestBody ArticleDTO articleDTO) {
        articleService.publishArticle(articleDTO);
        return ResponseResult.success();
    }

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取文章列表")
    @GetMapping("/list")
    public ResponseResult listArticle(
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return ResponseResult.success(articleService.listArticle(pageNum, pageSize, fuzzyField));
    }

    /**
     * 通过id获取文章详情
     *
     * @param articleId 文章id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取文章详情")
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long articleId) {
        return ResponseResult.success(articleService.getArticleById(articleId));
    }

    /**
     * 更新文章
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新文章")
    @PutMapping
    public ResponseResult updateArticle(@Validated(value = {VG.Update.class}) @RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);
        return ResponseResult.success();
    }

    /**
     * 更新文章分类
     *
     * @param map 键值对
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新文章分类")
    @PostMapping("/updateCategory")
    public ResponseResult updateArticleCategory(@RequestBody Map<String, Object> map) {
        Object mapId = map.get("id");
        Object mapCategoryName = map.get("categoryName");
        if (ObjUtil.hasEmpty(mapId, mapCategoryName)) {
            return ResponseResult.fail(AppHttpCodeEnum.BAD_REQUEST);
        }
        Long articleId = Long.valueOf(String.valueOf(mapId));
        String categoryName = String.valueOf(mapCategoryName);
        articleService.updateArticleCategory(articleId, categoryName);
        return ResponseResult.success();
    }

    /**
     * 更新标签列表
     * TODO 文章标签应该设置上限，如果超过上限就只能删除不能添加
     *
     * @param map 键值对
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新标签列表")
    @PostMapping("/updateTags")
    public ResponseResult updateArticleTags(@RequestBody Map<String, Object> map) {
        Object mapId = map.get("id");
        Object mapTags = map.get("tags");
        if (ObjUtil.hasEmpty(mapId, mapTags) || !(mapTags instanceof Collection<?>)) {
            return ResponseResult.fail(AppHttpCodeEnum.BAD_REQUEST);
        }
        Long articleId = Long.valueOf(String.valueOf(mapId));
        List<String> tags = JSONUtil.toList(JSONUtil.toJsonStr(mapTags), String.class);
        articleService.updateArticleTags(articleId, tags);
        return ResponseResult.success();
    }

    /**
     * 删除文章
     *
     * @param articleId 文章id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除文章")
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable(value = "id") Long[] articleId) {
        // article_tag表数据也要删除
        articleService.deleteArticle(articleId);
        return ResponseResult.success();
    }
}
