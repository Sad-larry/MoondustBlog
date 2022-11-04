package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;

/**
 * 博客文章表(Article)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:01
 */
public interface ArticleService extends IService<Article> {

    /**
     * 发表文章
     *
     * @param article 文章
     * @return {@link Long}
     */
    Long publishArticle(Article article);

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVo}<{@link ArticleVo}>
     */
    PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 通过id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleVo}
     */
    ArticleVo getArticleById(Long articleId);
}

