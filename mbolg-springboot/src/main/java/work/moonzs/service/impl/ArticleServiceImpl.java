package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.web.ArticleBaseVO;
import work.moonzs.domain.vo.web.ArticleInfoVO;
import work.moonzs.domain.vo.web.ArticlePreviewVO;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.ArticleService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.CommentService;
import work.moonzs.service.TagService;

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
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

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
    public PageVO<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Object> page = new Page<>(pageNum, pageSize);
        List<ArticleVo> articleVos = getBaseMapper().listArticle(page, fuzzyField);
        return new PageVO<>(articleVos, page.getTotal());
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

    @Override
    public boolean topArticle(Long articleId, Integer isStick) {
        LambdaUpdateWrapper<Article> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(Article::getIsStick, isStick);
        queryWrapper.eq(Article::getId, articleId);
        return update(queryWrapper);
    }

    // ----- 前端接口部分 -----

    @Override
    public Long articleCount() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublish, StatusConstants.NORMAL);
        return count(queryWrapper);
    }

    @Override
    public PageVO<ArticlePreviewVO> listWebArticle(Integer pageNum, Integer pageSize, Long categoryId, Long tagId) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        List<ArticlePreviewVO> articlePreviews = baseMapper.listPreviewPage(page, categoryId, tagId);
        articlePreviews.forEach(item -> {
            item.setTagVOList(tagService.getBlogTagsByArticleId(item.getId()));
        });
        return new PageVO<>(articlePreviews, page.getTotal());
    }

    @Override
    public ArticleInfoVO getArticleInfo(Long articleId) {
        // 根据 ID 查询文章
        ArticleInfoVO blogArticle = BeanCopyUtil.copyBean(getById(articleId), ArticleInfoVO.class);
        // 设置分类
        blogArticle.setCategory(categoryService.getBlogCategoryById(blogArticle.getCategoryId()));
        // 设置标签
        blogArticle.setTagList(tagService.getBlogTagsByArticleId(blogArticle.getId()));
        // 评论
        blogArticle.setComments(commentService.listArticleComment(blogArticle.getId()));
        // 最新文章
        blogArticle.setNewestArticleList(getNewestArticles(blogArticle.getId()));
        // 查询上一篇下一篇文章
        blogArticle.setLastArticle(getNextOrLastArticle(blogArticle.getId(), StatusConstants.LAST_ARTICLE));
        blogArticle.setNextArticle(getNextOrLastArticle(blogArticle.getId(), StatusConstants.NEXT_ARTICLE));
        // 相关推荐
        blogArticle.setRecommendArticleList(listRecommendArticle(blogArticle.getId()));
        // TODO redis 封装点赞量和浏览量
        // TODO redis 增加文章阅读量
        return blogArticle;
    }

    @Override
    public List<ArticleBaseVO> getNewestArticles(Long articleId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getAvatar, Article::getCreateTime)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                .ne(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last(StatusConstants.LIMIT_FIVE);
        return BeanCopyUtil.copyBeanList(list(queryWrapper), ArticleBaseVO.class);
    }

    @Override
    public ArticleBaseVO getNextOrLastArticle(Long articleId, Integer type) {
        boolean queryLast = StatusConstants.LAST_ARTICLE.equals(type);
        boolean queryNext = StatusConstants.NEXT_ARTICLE.equals(type);
        // 查询上一篇文章和查询下一篇文章，同为真或同为假则返回失败
        if (queryLast == queryNext) {
            BusinessAssert.fail(AppHttpCodeEnum.BLOG_NOT_EXIST);
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getAvatar)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                // 当查询上一篇文章时，条件符合 id < this.id
                .lt(queryLast, Article::getId, articleId)
                .gt(queryNext, Article::getId, articleId)
                // 当查询上一篇文章，条件符合 id 依次递减找最大值，否则 id 依次递增找最小值
                .orderByDesc(queryLast, Article::getId)
                .orderByAsc(queryNext, Article::getId)
                .last(StatusConstants.LIMIT_ONE);
        // 当有多个或没有时不抛出异常
        Article one = getOne(queryWrapper, false);
        return ObjUtil.isNotNull(one) ? BeanCopyUtil.copyBean(one, ArticleBaseVO.class) : null;
    }

    @Override
    public List<ArticleBaseVO> listRecommendArticle(Long articleId) {
        return baseMapper.listRecommendArticle(articleId);
    }

    @Override
    public PageVO<ArticleBaseVO> getArchives(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateTime)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                .orderByDesc(Article::getCreateTime);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), ArticleBaseVO.class), page.getTotal());
    }
}

