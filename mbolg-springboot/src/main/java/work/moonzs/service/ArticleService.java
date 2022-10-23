package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;

import java.util.List;

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
     * @return {@link List}<{@link ArticleVo}>
     */
    @Transactional
    PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField);
}

