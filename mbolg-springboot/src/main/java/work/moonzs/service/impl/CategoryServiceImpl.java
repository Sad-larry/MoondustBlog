package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Category;
import work.moonzs.enums.StatusConstants;
import work.moonzs.mapper.CategoryMapper;
import work.moonzs.service.CategoryService;

/**
 * (Category)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    /**
     * 通过id查询分类是否存在
     *
     * @param categoryId 类别id
     * @return boolean
     */
    @Override
    public boolean isExistCategoryById(Long categoryId) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, categoryId);
        queryWrapper.eq(Category::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count > 0;
    }
}

