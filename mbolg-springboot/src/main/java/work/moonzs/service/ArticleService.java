package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleListVo;
import work.moonzs.domain.vo.PageVo;

/**
 * (Article)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:03
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
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> listArticles(Integer pageNum, Integer pageSize, String fuzzyField);
}

