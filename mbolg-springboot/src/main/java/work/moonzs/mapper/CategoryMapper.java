package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.vo.web.CategoryVO;

import java.util.List;

/**
 * 博客分类表(Category)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:32:53
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 前端博客分类列表
     * 查询分类的 id,name 以及使用分类的文章数 articleNum
     *
     * @return {@link List}<{@link CategoryVO}>
     */
    List<CategoryVO> listWebCategory();
}

