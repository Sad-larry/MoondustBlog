package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.RoleMenu;
import work.moonzs.mapper.RoleMenuMapper;
import work.moonzs.service.RoleMenuService;

import java.util.List;

/**
 * 角色-权限资源关联表(RoleMenu)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:24
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public void updateRoleMenu(Long roleId, List<Long> permsIds) {
        if (permsIds.isEmpty()) {
            return;
        }
        List<RoleMenu> roleMenus = permsIds.stream().map(permsId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(permsId);
            return roleMenu;
        }).toList();
        saveBatch(roleMenus);
    }

    @Override
    public void deleteRoleMenuById(List<Long> roleIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleMenu::getRoleId, roleIds);
        remove(queryWrapper);
    }
}

