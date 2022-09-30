package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.MenuListVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.enums.StatusConstants;
import work.moonzs.mapper.MenuMapper;
import work.moonzs.service.MenuService;
import work.moonzs.utils.BeanCopyUtils;

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
     * 菜单列表
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

    /**
     * 存在菜单通过id吗
     *
     * @param menuId 菜单id
     * @return boolean
     */
    @Override
    public boolean isExistMenuById(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, menuId);
        queryWrapper.eq(Menu::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count > 0;
    }

    /**
     * 通过菜单名字和路径判断是否存在
     *
     * @param menuName 菜单名称
     * @param path     路径
     * @return boolean
     */
    @Override
    public boolean isExistMenuByCxNamePath(String menuName, String path) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, menuName);
        queryWrapper.eq(Menu::getPath, path);
        // 不同判断状态
        long count = count(queryWrapper);
        return count > 0;
    }
}

