package work.moonzs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.ArticleTag;
import work.moonzs.domain.vo.ArticleListVo;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.service.ArticleService;
import work.moonzs.service.ArticleTagService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.TagService;
import work.moonzs.utils.BeanCopyUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Moondust月尘
 */
@RestController(value = "AdminArtileC")
@RequestMapping(value = "/admin/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 发表文章
     *
     * @param articleDTO 文章dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult<?> publishArticle(@RequestBody ArticleDTO articleDTO) {
        // TODO 字段校验
        // 应该还要判断当前的分类和标签是否存在于数据库中，这两项默认是不会有问题的，就是怕有人不通过前端请求
        //  或者请求的时候刚好被删除了
        // 判断当前分类是否存在
        boolean existCategory = categoryService.isExistCategoryById(articleDTO.getCategoryId());
        if (!existCategory) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        // 判断当前tag是否存在
        // TODO 判断列表不为空，可以用工具包判断
        boolean existTags = false;
        if (!(articleDTO.getTagList() == null) && !(articleDTO.getTagList().size() == 0)) {
            existTags = tagService.isExistTagById(articleDTO.getTagList());
            if (!existTags) {
                return ResponseResult.fail(AppHttpCodeEnum.TAG_NOT_EXIST);
            }
        }
        // 发布文章，当文章发布成功时才添加标签
        Article article = BeanCopyUtils.copyBean(articleDTO, Article.class);
        // TODO userid没有值，测试时，默认为1
        article.setUserId(1L);
        Long articleId = articleService.publishArticle(article);
        // 添加标签，存个空标签进去还不如不存
        if (existTags) {
            List<ArticleTag> articleTags = articleDTO.getTagList().stream()
                    .map(tagId -> new ArticleTag(null, articleId, tagId))
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTags);
        }
        return ResponseResult.success();
    }

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ArticleListVo}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listArticles(
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        // 彩蛋，也是隐藏命令吧
        Long id = null;
        if (fuzzyField.startsWith("#id=")) {
            id = Long.parseLong(fuzzyField.substring(4));
        }
        return articleService.listArticles(pageNum, pageSize, id);
    }
}
