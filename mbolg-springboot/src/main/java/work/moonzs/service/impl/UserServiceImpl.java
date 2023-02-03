package work.moonzs.service.impl;

import cn.hutool.http.useragent.UserAgent;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.IpUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.base.web.service.IOnlineUserService;
import work.moonzs.base.web.service.ISaveUserService;
import work.moonzs.base.web.service.ITokenService;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysOnlineUserVO;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;
import work.moonzs.mapper.UserAuthMapper;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.SystemConfigService;
import work.moonzs.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户基础信息表(User)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:30
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ITokenService iTokenService;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private IOnlineUserService iOnlineUserService;
    @Autowired
    private ISaveUserService iSaveUserService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public String adminLogin(String username, String password, String uuid, String code) {
        // 是否开启了登录验证功能
        boolean enabledCheck = systemConfigService.selectCaptchaEnabled();
        if (enabledCheck) {
            // 验证验证码是否正确
            validateCaptcha(uuid, code);
        }
        // SpringSecurity登录认证
        // 认证不通过时，SpringSecurity会主动抛出异常
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 更新登录用户信息
        getLoginUserInfo(loginUser);
        // 为 loginUser 创建 token 并赋予 redis 缓存的唯一uuid键
        String token = iTokenService.createToken(loginUser);
        iSaveUserService.saveUserToken(loginUser.getUserUid());
        return token;
    }

    /**
     * 验证验证码
     *
     * @param uuid uuid
     * @param code 代码
     */
    private void validateCaptcha(String uuid, String code) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = (String) redisCache.get(verifyKey);
        redisCache.del(verifyKey);
        BusinessAssert.notNull(captcha, AppHttpCodeEnum.CAPTCHA_FAIL);
        BusinessAssert.equals(code, captcha, true, AppHttpCodeEnum.CAPTCHA_FAIL);
    }

    @Override
    public void adminLogout() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        // 从redis中删除用户token以及用户login信息
        iTokenService.delLoginUser(loginUser.getUserUid());
        // 用户主动退出登录时，直接删除redis离线用户，token也同时失效
        iOnlineUserService.userOffline(loginUser.getUserUid());
        iSaveUserService.removeUserToken(loginUser.getUserUid());
    }

    @Override
    public PageVO<SysUserVO> listUser(Integer pageNum, Integer pageSize, String username, Integer loginType) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<SysUserVO> userVOList = baseMapper.listUserPage(page, username, loginType);
        return new PageVO<>(userVOList, page.getTotal());
    }

    @Override
    public Long saveUser(User user) {
        user.setPassword(user.getPassword());
        // 保存用户返回用户id，并添加角色
        baseMapper.insert(user);
        return user.getId();
    }

    @Override
    public SysUserBaseVO getUserById(Long userId) {
        return baseMapper.getUserById(userId);
    }

    @Override
    public boolean deleteUser(Long[] userIds) {
        userAuthMapper.deleteByUserIds(userIds);
        return removeBatchByIds(List.of(userIds));
    }

    @Override
    public PageVO<SysOnlineUserVO> listOnlineUsers() {
        return iOnlineUserService.userOnlineList();
    }

    @Override
    public void kick(String userUid) {
        iOnlineUserService.userOffline(userUid);
        // 重新登录
        iSaveUserService.removeUserToken(userUid);
    }

    @Override
    public boolean updateLoginInfo(Long userId, String ipAddress, String ipSource, String os, String browser) {
        User user = new User();
        user.setId(userId);
        user.setIpAddress(ipAddress);
        user.setIpSource(ipSource);
        user.setOs(os);
        user.setBrowser(browser);
        return updateById(user);
    }

    public void getLoginUserInfo(LoginUser loginUser) {
        //修改登录信息
        String ipAddress = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getCityInfo(ipAddress);
        UserAgent userAgent = IpUtil.getUserAgent(request);
        // 解析客户端操作系统类型
        String os = userAgent.getOs().toString();
        // 解析浏览器
        String browser = userAgent.getBrowser().toString();
        User user = loginUser.getUser();
        user.setIpAddress(ipAddress);
        user.setIpSource(ipSource);
        user.setOs(os);
        user.setBrowser(browser);
        threadPoolTaskExecutor.execute(() -> updateLoginInfo(loginUser.getUser().getId(), ipAddress, ipSource, os, browser));
    }
}

