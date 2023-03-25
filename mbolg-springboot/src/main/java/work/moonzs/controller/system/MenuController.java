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
import work.moonzs.domain.dto.MenuDTO;
import work.moonzs.domain.entity.Menu;
import work.moonzs.service.MenuService;

/**
 * @author Moondust月尘
 */
@RestController("SystemMenuC")
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 菜单列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取菜单列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public ResponseResult listMenu(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(menuService.listMenu());
    }

    /**
     * 添加菜单
     *
     * @param menuDTO 菜单dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加菜单")
    @AdminOperationLogger(value = "添加菜单")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public ResponseResult addMenu(@Validated(VG.Insert.class) @RequestBody MenuDTO menuDTO) {
        menuService.insertMenu(BeanCopyUtil.copyBean(menuDTO, Menu.class));
        return ResponseResult.success();
    }


    /**
     * 更新菜单
     *
     * @param menuDTO 菜单dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新菜单")
    @AdminOperationLogger(value = "更新菜单")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:menu:update')")
    public ResponseResult updateMenu(@Validated(VG.Update.class) @RequestBody MenuDTO menuDTO) {
        menuService.updateMenu(BeanCopyUtil.copyBean(menuDTO, Menu.class));
        return ResponseResult.success();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除指定ID菜单")
    @AdminOperationLogger(value = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:menu:delete')")
    public ResponseResult deleteMenu(@PathVariable(value = "id") Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseResult.success();
    }
}
