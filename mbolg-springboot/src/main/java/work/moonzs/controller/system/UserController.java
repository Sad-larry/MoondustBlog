package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.UserService;

/**
 * @author Moondust月尘
 */
@RestController("SystemUserC")
@RequestMapping("/system/user")
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
    public ResponseResult getUserById(@PathVariable("id") Long userId) {
        return ResponseResult.success(userService.getUserById(userId));
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
    public ResponseResult deleteUser(@PathVariable(value = "ids") Long[] userIds) {
        userService.deleteUser(userIds);
        return ResponseResult.success();
    }


    /**
     * 在线用户列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "查看在线用户")
    @GetMapping(value = "/online")
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
    public ResponseResult kick(@RequestParam String userUid) {
        userService.kick(userUid);
        return ResponseResult.success();
    }
}
