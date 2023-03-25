package work.moonzs.controller.system;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.RoleDTO;
import work.moonzs.domain.dto.RolePermsDTO;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.sys.SysPermissionVO;
import work.moonzs.service.RoleService;

import java.util.List;

/**
 * @author Moondust月尘
 */
@RestController("SystemRoleC")
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 角色列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "角色列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public ResponseResult listRole(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String name) {
        return ResponseResult.success(roleService.listRole(pageNum, pageSize, name));
    }

    /**
     * 通过id查询角色详细信息
     *
     * @param roleId 角色id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询角色详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:role:info')")
    public ResponseResult getRoleById(@PathVariable(value = "id") Long roleId) {
        return ResponseResult.success(roleService.getRoleById(roleId));
    }


    /**
     * 添加角色
     *
     * @param roleDTO 角色dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加角色")
    @AdminOperationLogger(value = "添加角色")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    public ResponseResult addRole(@Validated(VG.Insert.class) @RequestBody RoleDTO roleDTO) {
        return ResponseResult.success(roleService.insertRole(BeanCopyUtil.copyBean(roleDTO, Role.class)));
    }

    /**
     * 更新角色
     *
     * @param roleDTO 角色dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新角色")
    @AdminOperationLogger(value = "更新角色")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:role:update')")
    public ResponseResult updateRole(@Validated(VG.Update.class) @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(BeanCopyUtil.copyBean(roleDTO, Role.class));
        return ResponseResult.success();
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据角色id进行批量删除操作")
    @AdminOperationLogger(value = "删除角色")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    public ResponseResult deleteRole(@PathVariable(value = "ids") Long[] roleIds) {
        roleService.deleteRole(roleIds);
        return ResponseResult.success();
    }

    /**
     * 获取角色权限
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取角色权限")
    @AdminOperationLogger(value = "获取角色权限")
    @GetMapping("/rolePerms")
    @PreAuthorize("@ss.hasPermi('system:role:rolePerms')")
    public ResponseResult getRolePermissions(@RequestParam Long roleId) {
        if (SecurityUtil.isAdminRole(roleId)) {
            return ResponseResult.success();
        }
        List<Long> rolePermsIds = roleService.getRolePermissionIds(roleId);
        List<SysPermissionVO> allPerms = roleService.getAllPermissions(true);
        return ResponseResult.success().put("allPerms", allPerms).put("rolePermsIds", rolePermsIds);
    }

    /**
     * 获取所有权限
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取所有权限")
    @AdminOperationLogger(value = "获取所有权限")
    @GetMapping("/allPerms")
    @PreAuthorize("@ss.hasPermi('system:role:allPerms')")
    public ResponseResult getAllPermissions() {
        return ResponseResult.success(roleService.getAllPermissions(true));
    }

    /**
     * 更新角色权限
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新角色权限")
    @AdminOperationLogger(value = "更新角色权限")
    @PostMapping("/updatePerms")
    @PreAuthorize("@ss.hasPermi('system:role:updatePerms')")
    public ResponseResult updateRolePerms(@RequestBody @Validated RolePermsDTO rolePermsDTO) {
        roleService.updateRolePermissions(rolePermsDTO.getId(), rolePermsDTO.getPerms());
        return ResponseResult.success();
    }
}
