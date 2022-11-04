package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.ArticleService;

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

    @Override
    public PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Object> page = new Page<>(pageNum, pageSize);
        List<ArticleVo> articleVos = getBaseMapper().listArticle(page, fuzzyField);
        return new PageVo<>(articleVos, page);
    }

    @Override
    public ArticleVo getArticleById(Long articleId) {
        ArticleVo articleVo = getBaseMapper().getArticleById(articleId);
        articleVo.setTagNames(tagMapper.getTagsByArticleId(articleId));
        return articleVo;
    }
}

