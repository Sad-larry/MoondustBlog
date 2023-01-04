package work.moonzs.controller.system;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.OperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.RoleDTO;
import work.moonzs.domain.entity.Role;
import work.moonzs.service.RoleService;

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
    public ResponseResult listRole(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String name) {
        return ResponseResult.success(roleService.listRole(pageNum, pageSize, name));
    }

    /**
     * TODO 通过id查询角色详细信息
     *
     * @param roleId 角色id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询角色详细信息")
    @GetMapping("/{id}")
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
    @OperationLogger(value = "添加角色")
    @PostMapping
    public ResponseResult addRole(@Validated(VG.Insert.class) @RequestBody RoleDTO roleDTO) {
        roleService.insertRole(BeanCopyUtil.copyBean(roleDTO, Role.class));
        return ResponseResult.success();
    }

    /**
     * 更新角色
     *
     * @param roleDTO 角色dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新角色")
    @OperationLogger(value = "更新角色")
    @PutMapping
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
    @OperationLogger(value = "删除角色")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteRole(@PathVariable(value = "ids") Long[] roleIds) {
        roleService.deleteRole(roleIds);
        return ResponseResult.success();
    }
}
