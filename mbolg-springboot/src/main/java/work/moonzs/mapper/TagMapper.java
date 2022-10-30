package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Tag;

import java.util.List;

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
     * @return {@link List}<{@link Tag}>
     */
    List<Tag> selectByArticleId(Long articleId);
}

