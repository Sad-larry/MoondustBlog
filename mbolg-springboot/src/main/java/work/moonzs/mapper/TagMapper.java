package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Tag;

import java.util.List;

/**
 * (Tag)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
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

