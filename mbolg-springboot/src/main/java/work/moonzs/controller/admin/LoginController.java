package work.moonzs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.entity.User;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.service.UserService;
import work.moonzs.utils.BeanCopyUtils;

/**
 * @author Moondust月尘
 */
@RestController("value = AdminLoginC")
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     *
     * @param loginUserDTO 登录用户dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping("/login")
    public ResponseResult<?> adminLogin(@RequestBody LoginUserDTO loginUserDTO) {
        // TODO 验证loginUserDTO字段，字段不能为空
        if (!StringUtils.hasText(loginUserDTO.getUserName()) &&
                !StringUtils.hasText(loginUserDTO.getUserName())) {
            return ResponseResult.fail(AppHttpCodeEnum.FIELD_EMPTY);
        }
        User user = BeanCopyUtils.copyBean(loginUserDTO, User.class);
        // TODO SpringSecurity登录认证
        return userService.adminLogin(user);
    }
}
