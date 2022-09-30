package work.moonzs.controller.admin;


import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.RoleDTO;
import work.moonzs.domain.entity.Role;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.enums.StatusConstants;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.service.RoleService;
import work.moonzs.utils.BeanCopyUtils;

/**
 * @author Moondust月尘
 */
@RestController(value = "AdminRoleC")
@RequestMapping("/admin/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;


    /**
     * 角色列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listRoles(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return roleService.listRoles(pageNum, pageSize);
    }

    /**
     * 添加角色
     *
     * @param roleDTO 角色dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult<?> addRole(@RequestBody RoleDTO roleDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        // TODO 其他校验，是否存在同名等等

        roleDTO.setId(null);
        Role role = BeanCopyUtils.copyBean(roleDTO, Role.class);
        roleMapper.insert(role);
        Long roleId = role.getId();
        // TODO 如果菜单id不为空，则把关联表的也加上去
        if (CollUtil.isNotEmpty(roleDTO.getMenuIds())) {
            // RoleMenuService.insert(new RoleMenu(null, roleId, menuIds.stream().forEach))
        }
        // TODO 如果资源id不为空，则把关联表的也加上去
        if (CollUtil.isNotEmpty(roleDTO.getResourceIds())) {
            // RoleResource.insert(new RoleResource(null, roleId, resourceIds.stream().forEach))
        }
        return ResponseResult.success();
    }


    /**
     * 更新作用
     *
     * @param roleDTO 角色dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult<?> updateRole(@RequestBody RoleDTO roleDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验

        // 判断该id角色是否存在
        boolean isExistRoleById = roleService.isExistRoleById(roleDTO.getId());
        if (!isExistRoleById) {
            return ResponseResult.fail(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }
        // 判断是否存在相同角色名
        Role byId = roleService.getById(roleDTO.getId());
        Role role = BeanCopyUtils.copyBean(roleDTO, Role.class);
        // 判断byId和role中的roleName, path是否相等
        if (role.equals(byId)) {
            return ResponseResult.fail(AppHttpCodeEnum.ROLE_EXIST);
        }
        roleService.updateById(role);
        return ResponseResult.success();
    }


    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteRole(@PathVariable(value = "id") Long roleId) {
        Role role = new Role();
        role.setId(roleId);
        role.setStatus(StatusConstants.DISABLE);
        roleService.updateById(role);
        return ResponseResult.success();
    }
}