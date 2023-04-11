package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.router.MetaVO;
import work.moonzs.domain.vo.router.RouterVO;
import work.moonzs.domain.vo.sys.SysMenuVO;
import work.moonzs.mapper.MenuMapper;
import work.moonzs.service.MenuService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限资源表 (Menu)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<SysMenuVO> listMenu() {
        List<Menu> childMenus = getChildMenus(selectMenuTreeAll(), 0L);
        return BeanCopyUtil.copyBeanList(childMenus, SysMenuVO.class);
    }

    @Override
    public boolean isExistMenuById(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, menuId);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean isExistMenuByCxNamePath(String name, String path) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getName, name);
        queryWrapper.eq(Menu::getUrl, path);
        // 不同判断状态
        return count(queryWrapper) > 0;
    }

    /**
     * 查询是否存在子菜单，如果查到了一个不为空，就返回true
     *
     * @param menuId 菜单id
     * @return boolean
     */
    public boolean isExistChildrenMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getType, "M");
        queryWrapper.eq(Menu::getParentId, menuId);
        return ObjUtil.isNotNull(getOne(queryWrapper));
    }

    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        List<Menu> menus;
        if (SecurityUtil.isAdmin(userId)) {
            menus = selectMenuTreeAll();
        } else {
            menus = getBaseMapper().selectMenuTreeByUserId(userId);
        }
        return getChildMenus(menus, 0L);
    }

    /**
     * 获取指定pid得根菜单
     *
     * @param menus 菜单
     * @param pid   pid
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> getChildMenus(List<Menu> menus, Long pid) {
        List<Menu> childList = getChildList(menus, pid);
        // 如果有子菜单，则进行递归
        if (!CollUtil.isEmpty(childList)) {
            childList = childList.stream().peek(menu -> {
                List<Menu> childMenus = getChildMenus(menus, menu.getId());
                menu.setChildren(childMenus);
            }).collect(Collectors.toList());
        }
        return childList;
    }

    /**
     * 获取指定pid得根菜单列表
     *
     * @param menus 菜单
     * @param pid   pid
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> getChildList(List<Menu> menus, Long pid) {
        return menus.stream()
                .filter(menu -> pid.equals(menu.getParentId()))
                // 通过OrderNum进行升序排序
                .sorted(Comparator.comparingInt(Menu::getSortNo))
                .collect(Collectors.toList());
    }


    @Override
    public List<RouterVO> buildMenus(List<Menu> menus) {
        // 为什么用链表集合，为了能按顺序输出，其实也没啥影响
        List<RouterVO> routerVOList = new LinkedList<>();
        menus.forEach(menu -> {
            RouterVO router = new RouterVO();
            router.setName(getRouterName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setHidden(getRouterHidden(menu));
            router.setSortNo(menu.getSortNo());
            router.setRedirect(menu.getRedirect());
            router.setMeta(new MetaVO(menu.getTitle(), menu.getIcon(), StatusConstants.NORMAL.equals(menu.getIsCache())));
            List<Menu> children = menu.getChildren();
            if (CollUtil.isNotEmpty(children) && StatusConstants.TYPE_DIR.equals(menu.getType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(children));
            }
            routerVOList.add(router);
        });
        return routerVOList;
    }

    @Override
    public boolean insertMenu(Menu menu) {
        BusinessAssert.isFalse(isExistMenuById(menu.getId()), AppHttpCodeEnum.MENU_EXIST);
        return save(menu);
    }

    @Override
    public boolean updateMenu(Menu menu) {
        // 修改的时候可以改id
        BusinessAssert.isTure(isExistMenuById(menu.getId()), AppHttpCodeEnum.MENU_NOT_EXIST);
        return updateById(menu);
    }

    @Override
    public boolean deleteMenu(Long menuId) {
        BusinessAssert.isFalse(isExistChildrenMenu(menuId), AppHttpCodeEnum.MENU_CHILDREN_EXIST);
        return removeById(menuId);
    }

    /**
     * 设置路由器是否隐藏,hidden=1则为true
     *
     * @param menu 菜单
     * @return {@link Boolean}
     */
    private Boolean getRouterHidden(Menu menu) {
        return StatusConstants.NORMAL.equals(menu.getHidden());
    }

    /**
     * 获取路由器地址
     *
     * @param menu 菜单
     * @return {@link String}
     */
    private String getRouterPath(Menu menu) {
        String routerPath = menu.getUrl();
        // 如果是一级目录，则需要加"/"
        if (0 == menu.getParentId().intValue() && StatusConstants.TYPE_DIR.equals(menu.getType())) {
            routerPath = StrUtil.prependIfMissing(menu.getUrl(), "/");
        }
        return routerPath;
    }

    /**
     * 获取路由器名字
     *
     * @param menu 菜单
     * @return {@link String}
     */
    private String getRouterName(Menu menu) {
        return StringUtils.capitalize(menu.getUrl());
    }

    /**
     * 查询所有菜单树，一般是管理员调用的
     *
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> selectMenuTreeAll() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 只选择目录和菜单部分
        queryWrapper.in(Menu::getType, "M");
        queryWrapper.orderByAsc(Menu::getParentId);
        queryWrapper.orderByAsc(Menu::getSortNo);
        return list(queryWrapper);
    }
}

