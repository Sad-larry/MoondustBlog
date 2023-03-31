package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
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
     * 列表分类
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取分类列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    public ResponseResult listCategory(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return ResponseResult.success(categoryService.listCategory(pageNum, pageSize, fuzzyField));
    }

    /**
     * 通过id查询分类详细信息
     *
     * @param categoryId 分类id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询分类详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:category:info')")
    public ResponseResult getCategoryById(@PathVariable(value = "id") Long categoryId) {
        return ResponseResult.success(categoryService.getCategoryById(categoryId));
    }

    /**
     * 添加分类
     *
     * @param categoryDTO 类别dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加分类")
    @AdminOperationLogger(value = "添加分类")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    public ResponseResult addCategory(@Validated(VG.Insert.class) @RequestBody CategoryDTO categoryDTO) {
        categoryService.insertCategory(BeanCopyUtil.copyBean(categoryDTO, Category.class));
        return ResponseResult.success();
    }


    /**
     * 更新分类
     *
     * @param categoryDTO 类别dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新分类")
    @AdminOperationLogger(value = "更新分类")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:category:update')")
    public ResponseResult updateCategory(@Validated(VG.Update.class) @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(BeanCopyUtil.copyBean(categoryDTO, Category.class));
        return ResponseResult.success();
    }

    /**
     * 删除分类
     * TODO 删除分类时，查询是否有文章引用，如果有，则不能删除，或者统一修改文章为别的分类
     *
     * @param categoryIds 分类id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据分类id进行批量删除操作")
    @AdminOperationLogger(value = "删除分类")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:category:delete')")
    public ResponseResult deleteCategory(@PathVariable(value = "ids") Long[] categoryIds) {
        categoryService.deleteCategory(categoryIds);
        return ResponseResult.success();
    }
}
