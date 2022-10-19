package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.base.utils.SecurityUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.MenuListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.router.MetaVo;
import work.moonzs.domain.vo.router.RouterVo;
import work.moonzs.mapper.MenuMapper;
import work.moonzs.service.MenuService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Menu)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 菜单列表，我用了别的方式实现
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> listMenus(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getStatus, StatusConstants.NORMAL);
        Page<Menu> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Menu> list = page.getRecords();
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(list, MenuListVo.class);
        PageVo<MenuListVo> pageVo = new PageVo<>(menuListVos, page.getTotal(), page.getCurrent(), page.getSize());
        return ResponseResult.success(pageVo);
    }

    @Override
    public boolean isExistMenuById(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, menuId);
        queryWrapper.eq(Menu::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean isExistMenuByCxNamePath(String name, String path) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, name);
        queryWrapper.eq(Menu::getPath, path);
        // 不同判断状态
        long count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        List<Menu> menus;
        if (SecurityUtils.isAdmin(userId)) {
            menus = selectMenuTreeAll();
        } else {
            menus = getBaseMapper().selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0L);
    }

    /**
     * 获取指定pid得根菜单
     *
     * @param menus 菜单
     * @param pid   pid
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> getChildPerms(List<Menu> menus, Long pid) {
        List<Menu> childList = getChildList(menus, pid);
        // 如果有子菜单，则进行递归
        if (!CollUtil.isEmpty(childList)) {
            childList = childList.stream().peek(menu -> {
                List<Menu> childPerms = getChildPerms(menus, menu.getId());
                menu.setChildren(childPerms);
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
                .filter(menu -> pid.equals(menu.getPid()))
                // 通过OrderNum进行升序排序
                .sorted((o1, o2) -> o2.getOrderNum() - o1.getOrderNum())
                .collect(Collectors.toList());
    }


    @Override
    public List<RouterVo> buildMenus(List<Menu> menus) {
        // 为什么用链表集合，为了能按顺序输出
        List<RouterVo> routerVoList = new LinkedList<>();
        menus.forEach(menu -> {
            RouterVo router = new RouterVo();
            router.setName(getRouterName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StatusConstants.DISABLE.equals(menu.getIsCache())));
            List<Menu> children = menu.getChildren();
            if (CollUtil.isNotEmpty(children) && StatusConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(children));
            }
            routerVoList.add(router);
        });
        return routerVoList;
    }

    /**
     * 获取路由器地址
     *
     * @param menu 菜单
     * @return {@link String}
     */
    private String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 如果是一级目录，则需要加"/"
        if (0 == menu.getPid().intValue() && StatusConstants.TYPE_DIR.equals(menu.getMenuType())) {
            routerPath = "/" + menu.getPath();
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
        return StringUtils.capitalize(menu.getPath());
    }

    /**
     * 查询所有菜单树，一般是管理员调用的
     *
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> selectMenuTreeAll() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 只选择目录和菜单部分
        queryWrapper.in(Menu::getMenuType, "M", "C");
        queryWrapper.eq(Menu::getStatus, StatusConstants.NORMAL);
        queryWrapper.orderByAsc(Menu::getPid);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        return list(queryWrapper);
    }
}

