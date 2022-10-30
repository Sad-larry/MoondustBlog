package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.CategoryVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.TagVo;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.ArticleService;
import work.moonzs.service.CategoryService;

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
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagMapper tagMapper;

    /**
     * 发表文章，返回文章的id，用于添加标签
     * 继承BaseMapper的mapper类使用insert方式时，在IdType=AUTO情况下，插入的article对象自动赋予对象id
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
     * 老老实实写模糊查询，不要总想着写彩蛋
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // fuzzyField不为空才进行模糊查询
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(Article::getTitle, fuzzyField);
            queryWrapper.or().like(Article::getSummary, fuzzyField);
        }
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 查询文章的tag
        List<Article> records = page.getRecords();
        // TODO 通过缓存获取当前的角色信息，给Article的username设置值
        List<ArticleVo> collect = records.stream().map(article -> {
            ArticleVo articleVo = BeanCopyUtil.copyBean(article, ArticleVo.class);
            // 通过categoryid查询分类
            Category category = categoryService.getById(article.getCategoryId());
            CategoryVo categoryVo = BeanCopyUtil.copyBean(category, CategoryVo.class);
            articleVo.setCategoryVo(categoryVo);
            // 通过tagid查询标签
            List<Tag> tags = tagMapper.selectByArticleId(article.getId());
            List<TagVo> tagVo = BeanCopyUtil.copyBeanList(tags, TagVo.class);
            articleVo.setTagListVo(tagVo);
            return articleVo;
        }).toList();
        // 分页封装
        return new PageVo<>(collect, page);
    }
}

