package work.moonzs.controller.system;

import cn.hutool.core.collection.CollUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.ValidateGroup;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.service.ArticleService;
import work.moonzs.service.ArticleTagService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.TagService;

import java.util.List;

/**
 * @author Moondust月尘
 */
@RestController("SystemArtileC")
@RequestMapping("/system/article")
public class ArticleController {
    // 建议用构造器注入而不是使用`@Autowired`注解
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    public ArticleController(ArticleService articleService, CategoryService categoryService, TagService tagService, ArticleTagService articleTagService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.articleTagService = articleTagService;
    }

    /**
     * 发表文章
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult publishArticle(@Validated(value = ValidateGroup.Insert.class) @RequestBody ArticleDTO articleDTO) {
        // 如果id不为空，应该为更新操作，这里的话就直接设置为空
        // 应该还要判断当前的分类和标签是否存在于数据库中，这两项默认是不会有问题的，就是怕有人不通过前端请求，
        //   或者请求的时候刚好被删除了
        // 判断当前分类是否存在
        boolean existCategory = categoryService.isExistCategoryById(articleDTO.getCategoryId());
        if (!existCategory) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        // 发布文章，当文章发布成功时才添加标签
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        // TODO userid没有值，测试时，默认为1
        article.setUserId(1L);

        Long articleId = articleService.publishArticle(article);
        System.out.println("文章id，看看是不是可以不用返回值：" + article.getId());
        // 添加标签，存个空标签进去还不如不存
        articleDTO.setId(articleId);
        updateArticleTag(articleDTO);
        return ResponseResult.success();
    }

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ArticleVo}>
     */
    @GetMapping("/list")
    public ResponseResult listArticle(
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        PageVo<ArticleVo> articleList = articleService.listArticle(pageNum, pageSize, fuzzyField);
        return ResponseResult.success(articleList);
    }

    /**
     * 更新文章
     * 这一块我觉得不能一下子全部更新，反正分类和标签这块不行
     * 也不是不行，总体还是需要的
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult updateArticle(@Validated(value = {ValidateGroup.Update.class}) @RequestBody ArticleDTO articleDTO) {
        boolean existCategory = categoryService.isExistCategoryById(articleDTO.getCategoryId());
        if (!existCategory) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        articleService.updateById(article);
        // 修改文章标签
        updateArticleTag(articleDTO);
        return ResponseResult.success();
    }

    /**
     * 更新文章分类
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping("/updateCategory")
    public ResponseResult updateArticleCategory(@RequestBody ArticleDTO articleDTO) {
        boolean existCategory = categoryService.isExistCategoryById(articleDTO.getCategoryId());
        if (!existCategory) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        Article article = new Article();
        article.setId(articleDTO.getId());
        article.setCategoryId(articleDTO.getCategoryId());
        articleService.updateById(article);
        return ResponseResult.success();
    }

    /**
     * 更新或添加标签条
     * TODO 文章标签应该设置上限，如果超过上限就只能删除不能添加
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping("/updateTags")
    public ResponseResult updateArticleTag(@RequestBody ArticleDTO articleDTO) {
        if (CollUtil.isNotEmpty(articleDTO.getTagList())) {
            boolean existTags = tagService.isExistTagByIds(articleDTO.getTagList());
            if (!existTags) {
                return ResponseResult.fail(AppHttpCodeEnum.TAG_NOT_EXIST);
            }
            // 通过文章id和标签集合更新标签
            Long id = articleDTO.getId();
            List<Long> tagList = articleDTO.getTagList();
            articleTagService.updateArticleTag(id, tagList);
            return ResponseResult.success();
        } else {
            return ResponseResult.success(AppHttpCodeEnum.WARNING_TAG_EMPTY);
        }
    }

    /**
     * 删除文章
     *
     * @param articleId 文章id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable(value = "id") Long articleId) {
        // article_tag表数据也要删除
        // articleService.removeById(articleId);
        // articleTagService.deleteArticleTagById(articleId);
        // 9-29，删除应该是设置状态为2
        // TODO 定时任务删除状态为2的文章，到时候在删除关联的tag表
        Article article = new Article();
        article.setId(articleId);
        article.setStatus(StatusConstants.DELETE);
        articleService.updateById(article);
        return ResponseResult.success();
    }
}
