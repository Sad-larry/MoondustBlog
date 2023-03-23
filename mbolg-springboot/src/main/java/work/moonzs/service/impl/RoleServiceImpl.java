package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysPermissionVO;
import work.moonzs.domain.vo.sys.SysRoleVO;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.service.RoleMenuService;
import work.moonzs.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色表(Role)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public PageVO<SysRoleVO> listRole(Integer pageNum, Integer pageSize, String name) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(name)) {
            page(page, new LambdaQueryWrapper<Role>().like(Role::getName, name));
        } else {
            page(page);
        }
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysRoleVO.class), page.getTotal());
    }

    @Override
    public boolean isExistRoleById(Long roleId) {
        return ObjUtil.isNotNull(getById(roleId));
    }

    @Override
    public boolean isExistRoleByName(String name) {
        return count(new LambdaQueryWrapper<Role>().eq(Role::getName, name)) > 0;
    }

    @Override
    public boolean isExistSameRoleByName(String name) {
        return count(new LambdaQueryWrapper<Role>().eq(Role::getName, name)) > 1;
    }

    @Override
    public SysRoleVO getRoleById(Long roleId) {
        return BeanCopyUtil.copyBean(getById(roleId), SysRoleVO.class);
    }

    @Override
    public Long insertRole(Role role) {
        BusinessAssert.isFalse(isExistRoleByName(role.getName()), AppHttpCodeEnum.ROLE_EXIST);
        baseMapper.insert(role);
        return role.getId();
    }

    @Override
    public boolean updateRole(Role role) {
        BusinessAssert.isTure(isExistRoleById(role.getId()), AppHttpCodeEnum.ROLE_NOT_EXIST);
        updateById(role);
        BusinessAssert.isFalse(isExistSameRoleByName(role.getName()), AppHttpCodeEnum.ROLE_EXIST);
        return true;
    }

    @Override
    public boolean deleteRole(Long[] roleIds) {
        roleMenuService.deleteRoleMenuById(List.of(roleIds));
        return removeBatchByIds(List.of(roleIds));
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        // 通过角色Id获取拥有的菜单栏权限
        return baseMapper.queryIdByRoleId(roleId);
    }

    @Override
    public List<SysPermissionVO> getAllPermissions(boolean buildTree) {
        // 获取所有权限
        List<SysPermissionVO> allPerms = baseMapper.queryByRoleId(null);
        if (buildTree) {
            return this.buildPermissionsTree(allPerms);
        }
        return allPerms;
    }

    @Override
    public List<SysPermissionVO> buildPermissionsTree(List<SysPermissionVO> permissions) {
        //    id 和 parent_id
        return getChildPerms(permissions, 0L);
    }

    @Override
    public boolean updateRolePermissions(Long roleId, Long[] perms) {
        BusinessAssert.isFalse(SecurityUtil.isAdminRole(roleId), "不能修改管理员权限");
        BusinessAssert.isTure(isExistRoleById(roleId), "角色不存在");
        // 若是修改角色权限，应该把权限删掉再添加
        roleMenuService.deleteRoleMenuById(List.of(roleId));
        roleMenuService.updateRoleMenu(roleId, List.of(perms));
        return true;
    }

    /**
     * 获得子权限
     *
     * @param permissions 权限
     * @param pid         pid
     * @return {@link List}<{@link SysPermissionVO}>
     */
    private List<SysPermissionVO> getChildPerms(List<SysPermissionVO> permissions, Long pid) {
        List<SysPermissionVO> childList = getChildList(permissions, pid);
        // 如果有子菜单，则进行递归
        if (!CollUtil.isEmpty(childList)) {
            childList = childList.stream().peek(perm -> {
                List<SysPermissionVO> childPerms = getChildPerms(permissions, perm.getId());
                perm.setChildren(childPerms);
            }).collect(Collectors.toList());
        }
        return childList;
    }

    /**
     * 获得子权限列表
     *
     * @param permissions 权限
     * @param pid         pid
     * @return {@link List}<{@link SysPermissionVO}>
     */
    private List<SysPermissionVO> getChildList(List<SysPermissionVO> permissions, Long pid) {
        return permissions.stream()
                .filter(perms -> pid.equals(perms.getParentId()))
                .collect(Collectors.toList());
    }
}

