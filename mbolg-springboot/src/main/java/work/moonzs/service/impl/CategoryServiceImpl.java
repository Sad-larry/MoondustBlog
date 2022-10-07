package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Category;
import work.moonzs.domain.vo.CategoryListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.mapper.CategoryMapper;
import work.moonzs.service.CategoryService;
import work.moonzs.base.utils.BeanCopyUtils;

import java.util.List;

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

    /**
     * 通过名称查询同名是否存在
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

    /**
     * 类别列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> listCategorys(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(Category::getCategoryName, fuzzyField);
            queryWrapper.or().like(Category::getDescription, fuzzyField);
        }
        queryWrapper.eq(Category::getStatus, StatusConstants.NORMAL);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Category> list = page.getRecords();
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(list, CategoryListVo.class);
        PageVo<CategoryListVo> pageVo = new PageVo<>(categoryListVos, page.getTotal(), page.getCurrent(), page.getSize());
        return ResponseResult.success(pageVo);
    }
}

