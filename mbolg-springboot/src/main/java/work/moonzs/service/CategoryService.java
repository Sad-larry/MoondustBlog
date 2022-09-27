package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}

