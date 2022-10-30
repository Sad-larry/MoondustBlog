package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.vo.CategoryVo;
import work.moonzs.domain.vo.PageVo;

/**
 * 博客分类表(Category)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:19
 */
public interface CategoryService extends IService<Category> {

    /**
     * 通过id查询分类是否存在并且状态为正常使用
     *
     * @param categoryId 类别id
     * @return boolean
     */
    @Transactional
    boolean isExistCategoryById(Long categoryId);

    /**
     * 通过名称查询同名是否存在
     *
     * @param categoryName 类别名称
     * @return boolean
     */
    boolean isExistCategoryByCategoryName(String categoryName);

    /**
     * 类别列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVo}<{@link CategoryVo}>
     */
    PageVo<CategoryVo> listCategory(Integer pageNum, Integer pageSize, String fuzzyField);
}

