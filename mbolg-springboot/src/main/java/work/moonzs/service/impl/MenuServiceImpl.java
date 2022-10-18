package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.base.utils.SecurityUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.MenuListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.mapper.MenuMapper;
import work.moonzs.service.MenuService;

import java.util.List;

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
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = selectMenuTreeAll();
        } else {
            menus = getBaseMapper().selectMenuTreeByUserId(userId);
        }
        // return getChildPerms(menus, 0);
        return menus;
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

