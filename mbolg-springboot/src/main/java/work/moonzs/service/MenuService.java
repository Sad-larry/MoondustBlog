package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.MenuListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.router.RouterVo;

import java.util.List;

/**
 * (Menu)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface MenuService extends IService<Menu> {

    /**
     * 菜单列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVo}<{@link MenuListVo}>
     */
    PageVo<MenuListVo> listMenus(Integer pageNum, Integer pageSize);

    /**
     * 通过id查询menu是否存在
     *
     * @param menuId 菜单id
     * @return boolean
     */
    boolean isExistMenuById(Long menuId);

    /**
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
}

