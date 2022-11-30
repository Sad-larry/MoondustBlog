package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.web.ArticleBaseVO;
import work.moonzs.domain.vo.web.ArticlePreviewVO;

import java.util.List;

/**
 * 博客文章表(Article)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:25:34
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章列表
     *
     * @param page       页面
     * @param fuzzyField 模糊领域
     * @return {@link List}<{@link ArticleVo}>
     */
    List<ArticleVo> listArticle(@Param("page") Page<Object> page, @Param("fuzzyField") String fuzzyField);

    /**
     * 通过id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleVo}
     */
    ArticleVo getArticleById(Long articleId);

    /**
     * 文章预览列表页面
     *
     * @param page       页面
     * @param categoryId 分类id
     * @param tagId      标签id
     * @return {@link List}<{@link ArticlePreviewVO}>
     */
    List<ArticlePreviewVO> listPreviewPage(@Param("page") Page<Article> page, @Param("categoryId") Long categoryId, @Param("tagId") Long tagId);

    /**
     * 推荐文章列表
     *
     * @param articleId 文章id
     * @return {@link List}<{@link ArticleBaseVO}>
     */
    List<ArticleBaseVO> listRecommendArticle(Long articleId);
}

