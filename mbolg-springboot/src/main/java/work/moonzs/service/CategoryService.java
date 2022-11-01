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
    boolean isExistCategoryById(Long categoryId);

    /**
     * 通过名称查询同名是否存在
     *
     * @param categoryName 类别名称
     * @return boolean
     */
    boolean isExistCategoryByCategoryName(String categoryName);

    /**
     * 列表分类
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVo}<{@link CategoryVo}>
     */
    PageVo<CategoryVo> listCategory(Integer pageNum, Integer pageSize, String fuzzyField);


    /**
     * 通过id查询分类详细信息
     *
     * @param categoryId 分类id
     * @return {@link CategoryVo}
     */
    CategoryVo getCategoryById(Long categoryId);

    /**
     * 插入分类
     *
     * @param category 分类
     * @return boolean
     */
    @Transactional
    boolean insertCategory(Category category);

    /**
     * 更新分类
     *
     * @param category 分类
     * @return boolean
     */
    @Transactional
    boolean updateCategory(Category category);

    /**
     * 删除分类
     *
     * @param categoryIds 分类id
     * @return boolean
     */
    @Transactional
    boolean deleteCategory(Long[] categoryIds);
}

