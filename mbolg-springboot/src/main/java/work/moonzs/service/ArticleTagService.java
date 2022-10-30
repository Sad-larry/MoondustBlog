package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.ArticleTag;

import java.util.List;

/**
 * 博客-标签关联表(ArticleTag)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:14
 */
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 更新标签条
     *
     * @param tagList   标记列表
     * @param articleId 文章id
     */
    void updateArticleTag(Long articleId, List<Long> tagList);

    /**
     * 通过id删除文章关联的标签
     *
     * @param articleId 文章id
     */
    void deleteArticleTagById(Long articleId);
}

