package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.RoleMenu;

import java.util.List;

/**
 * 角色-权限资源关联表(RoleMenu)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:50
 */
public interface RoleMenuService extends IService<RoleMenu> {
    /**
     * 更新角色权限
     *
     * @param roleId   角色id
     * @param permsIds 权限id
     */
    void updateRoleMenu(Long roleId, List<Long> permsIds);

    /**
     * 通过id删除角色关联的权限
     *
     * @param roleIds 角色id
     */
    void deleteRoleMenuById(List<Long> roleIds);
}

