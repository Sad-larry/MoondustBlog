package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.ArticleTag;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.ArticleListVo;
import work.moonzs.domain.vo.CategoryVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.TagVo;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.service.ArticleService;
import work.moonzs.service.ArticleTagService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.TagService;
import work.moonzs.utils.BeanCopyUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Article)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:03
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 发表文章，返回文章的id，用于添加标签
     * 继承BaseMapper的mapper类使用insert方式时，在IdType=AUTO情况下，插入的article对象自动赋予id
     *
     * @param article 文章
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public Long publishArticle(Article article) {
        articleMapper.insert(article);
        return article.getId();
    }

    /**
     * 文章列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param id       id
     * @return {@link ResponseResult}<{@link PageVo}>
     */
    @Override
    public ResponseResult<?> listArticles(Integer pageNum, Integer pageSize, Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // id不为空才进行该字段查询
        queryWrapper.eq(Objects.nonNull(id), Article::getId, id);
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 查询文章的tag
        List<Article> records = page.getRecords();
        List<ArticleListVo> collect = records.stream()
                .map(article -> {
                    ArticleListVo articleListVo = BeanCopyUtils.copyBean(article, ArticleListVo.class);
                    // 通过categoryid查询分类
                    Category byId = categoryService.getById(article.getCategoryId());
                    CategoryVo categoryVo = BeanCopyUtils.copyBean(byId, CategoryVo.class);
                    articleListVo.setCategoryVo(categoryVo);
                    // 通过tagid查询标签
                    LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(ArticleTag::getArticleId, article.getId());
                    List<ArticleTag> list = articleTagService.list(queryWrapper1);
                    List<TagVo> collect1 = list.stream()
                            .map(articleTag -> {
                                Tag byId1 = tagService.getById(articleTag.getTagId());
                                return BeanCopyUtils.copyBean(byId1, TagVo.class);
                            }).collect(Collectors.toList());
                    articleListVo.setTagListVo(collect1);
                    return articleListVo;
                }).toList();
        // 分页封装
        PageVo<ArticleListVo> pageVo = new PageVo<>(collect, page.getTotal(), page.getCurrent(), page.getSize());
        return ResponseResult.success(pageVo);
    }
}

