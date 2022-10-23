package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.vo.CategoryVo;
import work.moonzs.domain.vo.PageVo;
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

    @Override
    public boolean isExistCategoryById(Long categoryId) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, categoryId);
        queryWrapper.eq(Category::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count > 0;
    }

    /**
     * 通过名称查询同名是否存在
     * 不用判断状态，因为有人添加的可能和删除的分类同名了
     *
     * @param categoryName 类别名称
     * @return boolean
     */
    @Override
    public boolean isExistCategoryByCategoryName(String categoryName) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCategoryName, categoryName);
        // 不同判断状态
        long count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public PageVo<CategoryVo> listCategory(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(Category::getCategoryName, fuzzyField);
            queryWrapper.or().like(Category::getDescription, fuzzyField);
        }
        queryWrapper.eq(Category::getStatus, StatusConstants.NORMAL);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return new PageVo<>(BeanCopyUtil.copyBeanList(page.getRecords(), CategoryVo.class), page);
    }
}

