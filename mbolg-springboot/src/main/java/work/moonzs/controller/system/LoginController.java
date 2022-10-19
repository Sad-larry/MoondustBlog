package work.moonzs.controller.system;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.UserRoleInfo;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.base.utils.SecurityUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.CaptchaVo;
import work.moonzs.service.MenuService;
import work.moonzs.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Moondust月尘
 */
@RestController("SystemLoginC")
@RequestMapping("/system")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    /**
     * 管理员登录
     *
     * @param loginUserDTO 登录用户dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping("/login")
    public ResponseResult<?> adminLogin(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        User user = BeanCopyUtils.copyBean(loginUserDTO, User.class);
        // SpringSecurity登录认证
        // TODO 登录只为了拿去令牌
        return userService.adminLogin(user);
    }

    /**
     * 管理员注销
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping("/logout")
    public ResponseResult<?> adminLogout() {
        return userService.adminLogout();
    }


    /**
     * 获取路由器信息
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/getRouters")
    public ResponseResult<?> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取验证码图片并存入缓存
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/captchaImage")
    public ResponseResult<?> captchaImage() {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        // GifCaptcha captcha = CaptchaUtil.createGifCaptcha(200, 100, 1);
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 1, 1);
        String code = captcha.getCode();
        // TODO　将验证码结构存入redis,设置唯一标识，用户用这个唯一标识去redis中查找验证码进行验证
        UserRoleInfo.code = code;

        CaptchaVo captchaVo = new CaptchaVo();
        // TODO 是否开启验证应该从系统中读取判断
        captchaVo.setCaptchaEnabled(true);
        // TODO uuid暂时不写
        captchaVo.setUuid(code);
        captchaVo.setImg(captcha.getImageBase64());
        // 不用verify进行验证码验证，从redis读取存入的验证码以用户输入的验证码进行判断
        return ResponseResult.success(captchaVo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public ResponseResult<?> getInfo() {
        LoginUser loginUser = UserRoleInfo.user;
        System.out.println(loginUser);
        // TODO 这里应该是获取具体的用户信息
        return ResponseResult.success(loginUser);
    }
}
