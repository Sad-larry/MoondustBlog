package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticlePreviewVo;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVo;

import java.util.List;

/**
 * 博客文章表(Article)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:01
 */
public interface ArticleService extends IService<Article> {

    /**
     * 发表文章
     * 继承BaseMapper的mapper类使用insert方式时，在IdType=AUTO情况下，插入的article对象自动赋予对象id
     *
     * @param articleDTO 文章dto
     * @return boolean
     */
    @Transactional
    boolean publishArticle(ArticleDTO articleDTO);

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVo}<{@link ArticleVo}>
     */
    @Transactional(readOnly = true)
    PageVo<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 通过id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleVo}
     */
    ArticleVo getArticleById(Long articleId);

    @Transactional
    boolean updateArticle(ArticleDTO articleDTO);

    /**
     * 更新文章分类
     *
     * @param categoryName 分类名字
     * @param articleId    文章id
     * @return boolean
     */
    @Transactional
    boolean updateArticleCategory(Long articleId, String categoryName);

    /**
     * 更新标签列表
     *
     * @param articleId 文章id
     * @param tags      标签
     * @return boolean
     */
    @Transactional
    boolean updateArticleTags(Long articleId, List<String> tags);

    /**
     * 删除文章
     *
     * @param articleIds 文章ids
     * @return boolean
     */
    @Transactional
    boolean deleteArticle(Long[] articleIds);

    /**
     * 文章列表
     *
     * @param pageNum  1
     * @param pageSize 10
     * @return {@link PageVo}<{@link ArticleVo}>
     */
    @Transactional(readOnly = true)
    PageVo<ArticlePreviewVo> listWebArticle(Integer pageNum, Integer pageSize);
}

