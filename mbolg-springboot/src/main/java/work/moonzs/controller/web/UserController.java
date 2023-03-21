package work.moonzs.controller.web;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.annotation.WebOperationLogger;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.dto.RegisterUserDTO;
import work.moonzs.domain.dto.user.UserAuthDTO;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserAuth;
import work.moonzs.service.UserService;

import java.util.Map;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebUserC")
@RequestMapping(value = "/web/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "用户注册")
    @WebOperationLogger(value = "用户模块-用户注册", type = "添加", desc = "用户注册")
    // @PostMapping("/register")
    public ResponseResult addWebUser(@Validated @RequestBody RegisterUserDTO registerUserDTO) {
        // 如果是使用邮件登录的，则需要注册验证码
        // 用户是否已经注册
        if (userService.alreadyRegister(registerUserDTO.getUsername())) {
            return ResponseResult.fail(AppHttpCodeEnum.USER_ALREADY_REGISTER);
        }
        Map<String, Object> result = userService.sendRegisterMailCode(registerUserDTO.getUsername(), null, registerUserDTO.getMailCode());
        // 如果结果集为空，说明验证码验证没问题
        if (!ObjUtil.isNull(result)) {
            return ResponseResult.result(result);
        }
        userService.registerUser1(BeanCopyUtil.copyBean(registerUserDTO, User.class));
        return ResponseResult.success();
    }

    /**
     * 邮箱登录
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "邮箱登录")
    @WebOperationLogger(value = "用户模块-邮箱登录", type = "查询", desc = "用户登录")
    @PostMapping("/emailLogin")
    public ResponseResult emailLogin(@Validated @RequestBody LoginUserDTO loginUserDTO) {
        return ResponseResult.success(userService.userLogin(BeanCopyUtil.copyBean(loginUserDTO, User.class)));
    }

    /**
     * 微信小程序登录
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "微信小程序登录")
    @WebOperationLogger(value = "用户模块-微信小程序登录", type = "查询", desc = "用户登录")
    @GetMapping("/wxmpLogin")
    public ResponseResult wxmpLogin(@RequestParam String code) {
        String token = userService.wxmpLogin(code);
        // 登录成功返回令牌
        return ResponseResult.success().put(SystemConstants.TOKEN, token);
    }

    /**
     * 微信小程序用户信息
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "微信小程序用户信息")
    @WebOperationLogger(value = "用户模块-微信小程序信息", type = "查询", desc = "用户信息")
    @GetMapping("/wxmpUserInfo")
    public ResponseResult wxmpUserInfo() {
        return ResponseResult.success(userService.wxmpUserInfo());
    }

    /**
     * 用户退出登录
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @SystemLog(businessName = "用户退出登录")
    @WebOperationLogger(value = "用户模块-微信小程序注销", type = "删除", desc = "用户注销")
    @GetMapping("/wxmpLogout")
    public ResponseResult wxmpLogout() {
        userService.userLogout();
        return ResponseResult.success();
    }

    /**
     * 修改用户信息
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @SystemLog(businessName = "修改用户信息")
    @WebOperationLogger(value = "用户模块-微信小程序信息", type = "修改", desc = "修改信息")
    @PostMapping("/wxmpModify")
    public ResponseResult wxmpModify(@Validated @RequestBody UserAuthDTO userAuthDTO) {
        userService.wxmpModify(BeanCopyUtil.copyBean(userAuthDTO, UserAuth.class));
        return ResponseResult.success();
    }
}
