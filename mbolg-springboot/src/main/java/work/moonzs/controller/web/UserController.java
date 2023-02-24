package work.moonzs.controller.web;

import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.dto.RegisterUserDTO;
import work.moonzs.domain.entity.User;
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
    @PostMapping("/register")
    public ResponseResult addWebUser(@Validated @RequestBody RegisterUserDTO registerUserDTO) {
        // 如果是使用邮件登录的，则需要注册验证码
        if (registerUserDTO.getLoginType().equals(1)) {
            // 用户是否已经注册
            if (userService.alreadyRegister(registerUserDTO.getUsername())) {
                return ResponseResult.fail(AppHttpCodeEnum.USER_ALREADY_REGISTER);
            }
            Map<String, Object> result = userService.sendRegisterMailCode(registerUserDTO.getUsername(), registerUserDTO.getMailUuid(), registerUserDTO.getMailCode());
            // 如果结果集为空，说明验证码验证没问题
            if (!ObjUtil.isNull(result)) {
                return ResponseResult.result(result);
            }
        }
        userService.registerUser(BeanCopyUtil.copyBean(registerUserDTO, User.class));
        return ResponseResult.success();
    }

    /**
     * 邮箱登录
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "邮箱登录")
    @PostMapping("/emailLogin")
    public ResponseResult emailLogin(@Validated @RequestBody LoginUserDTO loginUserDTO) {
        return ResponseResult.success(userService.userLogin(BeanCopyUtil.copyBean(loginUserDTO, User.class)));
    }
}
