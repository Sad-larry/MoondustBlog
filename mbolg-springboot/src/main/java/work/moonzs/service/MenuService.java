package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.MenuVo;
import work.moonzs.domain.vo.router.RouterVo;

import java.util.List;

/**
 * 权限资源表 (Menu)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface MenuService extends IService<Menu> {

    /**
     * 菜单列表
     *
     * @return {@link List}<{@link MenuVo}>
     */
    List<MenuVo> listMenu();

    /**
     * 通过id查询menu是否存在
     *
     * @param menuId 菜单id
     * @return boolean
     */
    boolean isExistMenuById(Long menuId);

    /**
     * TODO DELETE
     * 通过菜单名字和路径判断是否存在
     *
     * @param name 菜单名称
     * @param path 路径
     * @return boolean
     */
    boolean isExistMenuByCxNamePath(String name, String path);

    /**
     * 通过用户id查询菜单树
     *
     * @param userId 用户id
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据菜单建立路由菜单
     *
     * @param menus 菜单
     * @return {@link List}<{@link RouterVo}>
     */
    List<RouterVo> buildMenus(List<Menu> menus);

    /**
     * 插入菜单
     *
     * @param menu 菜单
     * @return boolean
     */
    @Transactional
    boolean insertMenu(Menu menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单
     * @return boolean
     */
    @Transactional
    boolean updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return boolean
     */
    @Transactional
    boolean deleteMenu(Long menuId);
}

