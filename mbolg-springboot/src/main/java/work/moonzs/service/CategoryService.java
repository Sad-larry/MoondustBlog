package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Category;

/**
 * (Category)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface CategoryService extends IService<Category> {

    /**
     * 通过id查询分类是否存在
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
     * 类别列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> listCategorys(Integer pageNum, Integer pageSize, String fuzzyField);
}

