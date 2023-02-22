package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.RegisterUserDTO;
import work.moonzs.domain.entity.User;
import work.moonzs.service.UserService;

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
        userService.registerUser(BeanCopyUtil.copyBean(registerUserDTO, User.class));
        return ResponseResult.success();
    }
}
