package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.AddUserDTO;
import work.moonzs.domain.dto.UserDTO;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserRole;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.service.UserRoleService;
import work.moonzs.service.UserService;

/**
 * @author Moondust月尘
 */
@RestController("SystemUserC")
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 添加用户
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDTO addUserDTO) {
        addUserDTO.setId(null);
        User user = BeanCopyUtil.copyBean(addUserDTO, User.class);
        // TODO 密码应该加密存储
        Long userId = userService.saveUser(user);
        // 不存在roleId
        // TODO 如果说，普通用户是进不了后台的，他们也就不需要任何角色，数据库中的默认角色就是普通用户，更新的时候，罢了罢了
        if (addUserDTO.getRoleId() == 1L) {
            // 普通用户
            addUserDTO.setRoleId(2L);
        }
        userRoleService.save(new UserRole(null, userId, addUserDTO.getRoleId()));
        return ResponseResult.success();
    }

    /**
     * 用户列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        PageVo<UserListVo> userList = userService.listUsers(pageNum, pageSize, fuzzyField);
        return ResponseResult.success(userList);
    }

    /**
     * 更新用户，但是不能更新密码，到时候再说
     *
     * @param userDTO 用户dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult updateUser(@RequestBody UserDTO userDTO) {
        // TODO 校验userDTO都不为空

        // 管理员不能更新角色信息
        if (userDTO.getId() != 1L) {
            // TODO 判断角色是否存在

            // 通过用户名更新角色信息
            userRoleService.updateByUserId(userDTO.getId(), userDTO.getId());
        }
        // 管理员不能修改状态
        if (userDTO.getId() == 1L) {
            userDTO.setId(null);
        }
        User user = BeanCopyUtil.copyBean(userDTO, User.class);
        userService.updateById(user);
        return ResponseResult.success();
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable(value = "id") Long userId) {
        // 管理员不能被删
        if (userId == 1L) {
            return ResponseResult.fail();
        }
        User user = new User();
        user.setId(userId);
        user.setStatus(StatusConstants.DISABLE);
        userService.updateById(user);
        return ResponseResult.success();
    }
}
