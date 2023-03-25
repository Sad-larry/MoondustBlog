package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.user.UpdateUserAuthDTO;
import work.moonzs.domain.dto.user.UpdateUserPasswordDTO;
import work.moonzs.domain.dto.user.UpdateUserStatusDTO;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserAuth;
import work.moonzs.service.UserService;

/**
 * @author Moondust月尘
 */
@RestController("SystemUserC")
@RequestMapping("/system/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户列表
     *
     * @param pageNum   页面num
     * @param pageSize  页面大小
     * @param username  用户名
     * @param loginType 登录类型
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "用户列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    public ResponseResult listUser(
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String username,
            @RequestParam(defaultValue = "", required = false) Integer loginType) {
        return ResponseResult.success(userService.listUser(pageNum, pageSize, username, loginType));
    }

    /**
     * 获取用户基本信息
     *
     * @param userId 用户id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取用户基本信息")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:user:info')")
    public ResponseResult getUserById(@PathVariable("id") Long userId) {
        return ResponseResult.success(userService.getUserById(userId));
    }

    /**
     * 获取用户登录信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    @PreAuthorize("@ss.hasPermi('system:user:getInfo')")
    public ResponseResult getInfo() {
        return ResponseResult.success(userService.getLoginUserInfo());
    }

    /**
     * 更新用户状态
     *
     * @param updateUserStatusDTO 用户状态修改dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新用户状态")
    @AdminOperationLogger(value = "更新用户状态")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:user:updateStatus')")
    public ResponseResult updateUserStatus(@Validated @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {
        userService.updateUserStatus(BeanCopyUtil.copyBean(updateUserStatusDTO, User.class));
        return ResponseResult.success();
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除用户")
    @AdminOperationLogger(value = "删除用户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public ResponseResult deleteUser(@PathVariable(value = "ids") Long[] userIds) {
        userService.deleteUser(userIds);
        return ResponseResult.success();
    }

    /**
     * 更新用户密码（需要登录）
     *
     * @param updateUserPasswordDTO 更新用户密码dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "修改密码")
    @AdminOperationLogger(value = "修改密码")
    @PostMapping("/updatePassword")
    @PreAuthorize("@ss.hasPermi('system:user:updatePassword')")
    public ResponseResult updateLoginUserPassword(@RequestBody @Validated(VG.Update.class) UpdateUserPasswordDTO updateUserPasswordDTO) {
        // 防止使用自己的Token改别人的密码
        String username = SecurityUtil.getLoginUser().getUser().getUsername();
        // 新旧密码不一致
        userService.checkOldPassword(username, updateUserPasswordDTO.getOldPassword());
        userService.updateUserPassword(username, updateUserPasswordDTO.getNewPassword(), true);
        return ResponseResult.success();
    }

    /**
     * 在线用户列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "查看在线用户")
    @GetMapping(value = "/online")
    @PreAuthorize("@ss.hasPermi('system:user:online')")
    public ResponseResult listOnlineUsers() {
        return ResponseResult.successPageVO(userService.listOnlineUsers());
    }

    /**
     * 强制用户下线
     *
     * @param userUid 用户uid
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "强制用户下线")
    @AdminOperationLogger(value = "强制用户下线")
    @GetMapping(value = "/kick")
    @PreAuthorize("@ss.hasPermi('system:user:kick')")
    public ResponseResult kick(@RequestParam String userUid) {
        userService.kick(userUid);
        return ResponseResult.success();
    }

    /**
     * 获取用户详细信息
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取用户详细信息")
    @GetMapping("/profile")
    @PreAuthorize("@ss.hasPermi('system:userProfile:info')")
    public ResponseResult getUserProfile() {
        return ResponseResult.result(userService.getUserProfile());
    }

    /**
     * 更新用户信息
     *
     * @param updateUserAuthDTO 更新用户身份dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新用户信息")
    @AdminOperationLogger(value = "更新用户信息")
    @PutMapping("/profile")
    @PreAuthorize("@ss.hasPermi('system:userProfile:update')")
    public ResponseResult updateUserProfile(@Validated @RequestBody UpdateUserAuthDTO updateUserAuthDTO) {
        userService.updateUserProfile(BeanCopyUtil.copyBean(updateUserAuthDTO, UserAuth.class));
        return ResponseResult.success();
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新用户头像")
    @AdminOperationLogger(value = "更新用户头像")
    @PostMapping(path = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@ss.hasPermi('system:userProfile:updateAvatar')")
    public ResponseResult updateUserAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseResult.success(userService.updateUserAvatar(file));
    }
}
