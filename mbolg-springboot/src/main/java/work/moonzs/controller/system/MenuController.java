package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtils;
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
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listMenus(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return menuService.listMenus(pageNum, pageSize);
    }


    /**
     * 添加菜单
     *
     * @param menuDTO 菜单dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult<?> addMenu(@RequestBody MenuDTO menuDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验

        // 判断分类名是否有相同的，有就不添加
        boolean isExistMenu = menuService.isExistMenuByCxNamePath(menuDTO.getName(), menuDTO.getPath());
        if (isExistMenu) {
            // TODO 新增的话，如果新增的标签跟删除的标签是一样的话就将删除的标签状态设置为1
            return ResponseResult.fail(AppHttpCodeEnum.MENU_EXIST);
        }
        menuDTO.setId(null);
        Menu menu = BeanCopyUtils.copyBean(menuDTO, Menu.class);
        menuService.save(menu);
        return ResponseResult.success();
    }


    /**
     * 更新菜单
     *
     * @param menuDTO 菜单dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult<?> updateMenu(@RequestBody MenuDTO menuDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验

        // 判断该id分类是否存在
        boolean isExistMenuById = menuService.isExistMenuById(menuDTO.getId());
        if (!isExistMenuById) {
            return ResponseResult.fail(AppHttpCodeEnum.MENU_NOT_EXIST);
        }
        // 判断是否存在相同分类名
        Menu byId = menuService.getById(menuDTO.getId());
        Menu menu = BeanCopyUtils.copyBean(menuDTO, Menu.class);
        // 判断byId和menu中的menuName, path是否相等
        if (menu.equals(byId)) {
            return ResponseResult.fail(AppHttpCodeEnum.MENU_EXIST);
        }
        menuService.updateById(menu);
        return ResponseResult.success();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteMenu(@PathVariable(value = "id") Long menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        menu.setStatus(StatusConstants.DISABLE);
        menuService.updateById(menu);
        return ResponseResult.success();
    }
}
