package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.ArticleService;
import work.moonzs.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客文章表(Article)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:38:42
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean publishArticle(ArticleDTO articleDTO) {
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        article.setUserId(SecurityUtil.getUserId());
        // 查询分类名是否存在，存在则返回ID，否则新增分类再返回ID
        article.setCategoryId(categoryService.insertCategoryWithName(articleDTO.getCategoryName()));
        // 插入文章，若新增成功则插入文章标签关联表
        int insert = getBaseMapper().insert(article);
        if (insert > 0) {
            List<Long> tagList = getTagList(articleDTO.getTags());
            return tagMapper.saveTagsWithArticleId(article.getId(), tagList);
        }
        return false;
    }

    /**
     * 通过标签名，查询所有标签ID，并插入新的标签
     *
     * @param tags 标签
     * @return {@link List}<{@link Long}>
     */
    private List<Long> getTagList(List<String> tags) {
        List<Long> tagIds = new ArrayList<>();
        tags.forEach(name -> {
            Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
            if (tag == null) {
                tag = Tag.builder().name(name).build();
                tagMapper.insert(tag);
            }
            tagIds.add(tag.getId());
        });
        return tagIds;
    }

    @Override
    public PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Object> page = new Page<>(pageNum, pageSize);
        List<ArticleVo> articleVos = getBaseMapper().listArticle(page, fuzzyField);
        return new PageVo<>(articleVos, page);
    }

    @Override
    public ArticleVo getArticleById(Long articleId) {
        ArticleVo articleVo = getBaseMapper().getArticleById(articleId);
        BusinessAssert.notNull(articleVo, AppHttpCodeEnum.BLOG_NOT_EXIST);
        articleVo.setTagNames(tagMapper.getTagsByArticleId(articleId));
        return articleVo;
    }

    @Override
    public boolean updateArticle(ArticleDTO articleDTO) {
        validateArticle(articleDTO.getId());
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        article.setCategoryId(categoryService.insertCategoryWithName(articleDTO.getCategoryName()));
        updateById(article);
        // 文章标签修改，先删除所有标签再新增标签
        tagMapper.removeTagsByArticleIds(new Long[]{article.getId()});
        List<Long> tagList = getTagList(articleDTO.getTags());
        tagMapper.saveTagsWithArticleId(article.getId(), tagList);
        return true;
    }

    @Override
    public boolean updateArticleCategory(Long articleId, String categoryName) {
        validateArticle(articleId);
        Article article = new Article();
        article.setId(articleId);
        article.setCategoryId(categoryService.insertCategoryWithName(categoryName));
        return updateById(article);
    }

    @Override
    public boolean updateArticleTags(Long articleId, List<String> tags) {
        validateArticle(articleId);
        tagMapper.removeTagsByArticleIds(new Long[]{articleId});
        List<Long> tagList = getTagList(tags);
        tagMapper.saveTagsWithArticleId(articleId, tagList);
        return true;
    }

    @Override
    public boolean deleteArticle(Long[] articleIds) {
        tagMapper.removeTagsByArticleIds(articleIds);
        return removeBatchByIds(List.of(articleIds));
    }

    /**
     * 验证文章符合性
     *
     * @param articleId 文章id
     */
    public void validateArticle(Long articleId) {
        Article byId = getById(articleId);
        // 是否存在博客
        BusinessAssert.notNull(byId, AppHttpCodeEnum.BLOG_NOT_EXIST);
        // 若当前用户修改的不是自己的文章则发出警告
        if (!SecurityUtil.getUserId().equals(byId.getUserId())) {
            BusinessAssert.fail(AppHttpCodeEnum.WARNING_ARTICLE_BAD_REQUEST);
        }
    }
}

