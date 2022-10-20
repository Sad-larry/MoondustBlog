package work.moonzs.controller.system;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.CategoryDTO;
import work.moonzs.domain.entity.Category;
import work.moonzs.service.CategoryService;

/**
 * @author Moondust月尘
 */
@RestController("SystemCategoryC")
@RequestMapping("/system/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加类别
     *
     * @param categoryDTO 类别dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult<?> addCategory(@RequestBody CategoryDTO categoryDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        if (StrUtil.isBlank(categoryDTO.getCategoryName()) || StrUtil.isBlank(categoryDTO.getDescription())) {
            return ResponseResult.fail(AppHttpCodeEnum.FIELD_EMPTY);
        }
        // 判断分类名是否有相同的，有就不添加
        boolean isExistCategory = categoryService.isExistCategoryByCategoryName(categoryDTO.getCategoryName());
        if (isExistCategory) {
            // TODO 新增的话，如果新增的标签跟删除的标签是一样的话就将删除的标签状态设置为1
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_EXIST);
        }
        categoryDTO.setId(null);
        Category category = BeanCopyUtils.copyBean(categoryDTO, Category.class);
        categoryService.save(category);
        return ResponseResult.success();
    }


    /**
     * 列表分类
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listCategory(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return categoryService.listCategory(pageNum, pageSize, fuzzyField);
    }

    /**
     * 更新类别
     *
     * @param categoryDTO 类别dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        // tagName，description，status不为空
        if (StrUtil.isBlank(categoryDTO.getCategoryName()) || StrUtil.isBlank(categoryDTO.getDescription()) || StrUtil.isBlank(categoryDTO.getStatus())) {
            return ResponseResult.fail(AppHttpCodeEnum.FIELD_EMPTY);
        }
        // 判断该id分类是否存在
        boolean isExistCategoryId = categoryService.isExistCategoryById(categoryDTO.getId());
        if (!isExistCategoryId) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_NOT_EXIST);
        }
        // 判断是否存在相同分类名
        Category byId = categoryService.getById(categoryDTO.getId());
        Category category = BeanCopyUtils.copyBean(categoryDTO, Category.class);
        // 判断byId和category中的categoryName，description，status是否相等
        if (category.equals(byId)) {
            return ResponseResult.fail(AppHttpCodeEnum.CATEGORY_EXIST);
        }
        categoryService.updateById(category);
        return ResponseResult.success();
    }

    /**
     * 删除类别
     *
     * @param categoryId 类别id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteCategory(@PathVariable(value = "id") Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(StatusConstants.DISABLE);
        categoryService.updateById(category);
        return ResponseResult.success();
    }
}
