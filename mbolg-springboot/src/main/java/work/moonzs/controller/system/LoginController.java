package work.moonzs.controller.system;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.vo.CaptchaVo;
import work.moonzs.service.MenuService;
import work.moonzs.service.SystemConfigService;
import work.moonzs.service.UserService;

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
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 获取验证码图片并存入redis缓存
     * 将uuid存入redis缓存，前端用户通过uuid与缓存中保存的验证码进行匹配
     * 验证码设置有效期1小时，如果缓存中的验证码还没有验证，则失效
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/captchaImage")
    public ResponseResult captchaImage() {
        CaptchaVo captchaVo = new CaptchaVo();
        // 是否开启验证
        captchaVo.setCaptchaEnabled(false);
        boolean enabledCheck = systemConfigService.selectCaptchaEnabled();
        if (!enabledCheck) {
            // 如果不需要验证，则直接返回 false
            return ResponseResult.success(captchaVo);
        }
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 1, 1);
        String code = captcha.getCode();
        String uuid = IdUtil.fastSimpleUUID();
        // 将验证码结构存入redis,设置唯一标识，用户用这个唯一标识去redis中查找验证码进行验证，一个验证码10分钟有效时间
        redisCache.set(CacheConstants.CAPTCHA_CODE_KEY + uuid, code, 10 * 60L);
        captchaVo.setCaptchaEnabled(true);
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());
        // 不用verify进行验证码验证，从redis读取存入的验证码以用户输入的验证码进行判断
        return ResponseResult.success(captchaVo);
    }

    /**
     * 管理员登录
     *
     * @param loginUserDTO 登录用户dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @SystemLog(businessName = "管理员登录")
    @PostMapping("/login")
    public ResponseResult adminLogin(@Validated @RequestBody LoginUserDTO loginUserDTO) {
        // SpringSecurity登录认证
        String token = userService.adminLogin(loginUserDTO.getUsername(), loginUserDTO.getPassword(), loginUserDTO.getUuid(), loginUserDTO.getCode());
        // 登录只为了拿去令牌
        return ResponseResult.success().put(SystemConstants.TOKEN, token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        // TODO 这里应该是获取具体的用户信息
        return ResponseResult.success(loginUser);
    }

    /**
     * @return {@link ResponseResult}<{@link ?}>
     */
    @SystemLog(businessName = "获取路由器信息")
    @GetMapping("/getRouters")
    public ResponseResult getRouters() {
        Long userId = SecurityUtil.getUserId();
        List<Menu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseResult.success(menuService.buildMenus(menus));
    }

    /**
     * 管理员注销
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @SystemLog(businessName = "管理员注销")
    @PostMapping("/logout")
    public ResponseResult adminLogout() {
        userService.adminLogout();
        return ResponseResult.success();
    }

}
