package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.CategoryService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebCategoryC")
@RequestMapping(value = "/web/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 获取分类列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取分类列表")
    @GetMapping("/list")
    public ResponseResult listWebCategory() {
        return ResponseResult.success(categoryService.listWebCategory());
    }
}
