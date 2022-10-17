package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.ArticleTag;

import java.util.List;

/**
 * (ArticleTag)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
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

