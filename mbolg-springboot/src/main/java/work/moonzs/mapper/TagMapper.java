package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.web.TagVO;

import java.util.List;
import java.util.Map;

/**
 * 博客标签表(Tag)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:34:41
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 选择文章id查询文章的标签
     *
     * @param articleId 文章id
     * @return {@link List}<{@link TagVO}>
     */
    List<TagVO> selectByArticleId(Long articleId);

    /**
     * 通过文章id获取标签列表
     *
     * @param articleIds 文章id
     * @return {@link Map}<{@link Long}, {@link List}<{@link String}>>
     */
    List<Tag> listByArticleId(@Param("articleIds") List<Long> articleIds);

    /**
     * 通过文章id查询标签字符串
     *
     * @param articleId 文章id
     * @return {@link String}
     */
    String getTagsByArticleId(Long articleId);

    /**
     * 保存标签条id
     *
     * @param tagIds    标签ids
     * @param articleId 文章id
     * @return boolean
     */
    boolean saveTagsWithArticleId(@Param("articleId") Long articleId, @Param("tagIds") List<Long> tagIds);

    /**
     * 通过文章id删除标签
     *
     * @param articleIds 文章id
     * @return boolean
     */
    boolean removeTagsByArticleIds(@Param("articleIds") Long[] articleIds);
}

