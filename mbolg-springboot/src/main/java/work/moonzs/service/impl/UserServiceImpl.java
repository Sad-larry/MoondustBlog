package work.moonzs.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.qiniu.service.QiniuService;
import work.moonzs.base.utils.*;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.base.web.service.IOnlineUserService;
import work.moonzs.base.web.service.ISaveUserService;
import work.moonzs.base.web.service.ITokenService;
import work.moonzs.config.security.authentication.WxmpLoginAuthenticationToken;
import work.moonzs.domain.dto.user.WxmpUserDTO;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.MailEntity;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserAuth;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysOnlineUserVO;
import work.moonzs.domain.vo.sys.SysRoleVO;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;
import work.moonzs.domain.vo.web.UserInfoVO;
import work.moonzs.mapper.UserAuthMapper;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.RoleService;
import work.moonzs.service.SystemConfigService;
import work.moonzs.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private IMailUtil iMailUtil;
    @Autowired
    private QiniuService qiniuService;
    @Autowired
    private RoleService roleService;

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
    public void userLogout() {
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
    public List<UserInfoVO> getWebUserByIds(List<Long> userIds) {
        return baseMapper.getWebUserByIds(userIds);
    }

    @Override
    public boolean updateUserProfile(UserAuth userAuth) {
        int update = userAuthMapper.updateById(userAuth);
        return update > 0;
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 检查是否符合
        IFileUtil.isLegalFile(file);
        String name = file.getName();
        String fileKey = PathUtil.generateFilePath(file.getOriginalFilename());
        qiniuService.uploadFile(fileKey, file);
        String avatar = qiniuService.publicDownload(fileKey);
        Long userAuthId = SecurityUtil.getLoginUser().getUser().getUserAuthId();
        UserAuth userAuth = new UserAuth();
        userAuth.setId(userAuthId);
        userAuth.setAvatar(avatar);
        userAuthMapper.updateById(userAuth);
        return avatar;
    }

    @Override
    public boolean updateUserStatus(User user) {
        BusinessAssert.isFalse(SecurityUtil.isAdmin(user.getId()), "不能修改管理员信息");
        BusinessAssert.isFalse(SecurityUtil.isAdminRole(user.getRoleId()), "不能修改成管理员");
        BusinessAssert.isTure(roleService.isExistRoleById(user.getRoleId()), "角色信息不存在");
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long[] userIds) {
        List<Long> ids = Arrays.stream(userIds).filter(userId -> !SecurityUtil.isAdmin(userId)).toList();
        if (ids.isEmpty()) {
            BusinessAssert.fail("管理员不能被删除");
        }
        userAuthMapper.deleteByUserIds(ids.toArray(Long[]::new));
        return removeBatchByIds(ids);
    }

    @Override
    public void updatePasswordBySendEmail(String email) {
        // 首先查询用账号密码登录的用户表里是否有该邮箱注册的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, email);
        queryWrapper.eq(User::getLoginType, StatusConstants.LOGIN_TYPE_EMAIL);
        User one = getOne(queryWrapper);
        BusinessAssert.notNull(one, "用户没有注册，请直接注册");
        // 若有则判断是否被禁用了
        // 若禁用则不能修改密码，建议用别的邮箱注册
        BusinessAssert.isFalse(StatusConstants.DISABLE.equals(one.getStatus()), "该账号已被封禁，请联系管理员解封");
        // 否则直接发送邮件验证码修改密码
        String code = RandomUtil.randomNumbers(6);
        int timeout = 5;
        MailEntity mailEntity = MailEntity.builder().sendTo(email).subject("修改用户密码").text(iMailUtil.getUpdatePassword(email, code, timeout)).build();
        iMailUtil.sendHtmlEMail(mailEntity);
        // 一般都是用邮件注册账号的，除了小程序等其他渠道
        String uuid = DigestUtil.md5Hex(email.getBytes());
        redisCache.set(getPasswordMailCodeKey(uuid), code, timeout * 60L);
    }

    @Override
    public void updateUserPassword(String username, String newPassword, boolean online) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        // 密码加密
        updateWrapper.set(User::getPassword, SecurityUtil.encryptPassword(newPassword));
        updateWrapper.eq(User::getUsername, username);
        if (update(updateWrapper)) {
            // 如果在线则需要重新登录
            if (online) {
                // 清除用户数据，踢人下线
                String userUid = SecurityUtil.getLoginUser().getUserUid();
                kick(userUid);
            }
        }
    }

    @Override
    public void checkOldPassword(String username, String oldPassword) {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getPassword);
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);
        if (null == user) {
            BusinessAssert.fail("用户不存在");
        }
        String password = user.getPassword();
        BusinessAssert.isTure(SecurityUtil.matchesPassword(oldPassword, password), "用户密码错误");
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
    public boolean registerUser1(User user) {
        UserAuth userAuth = UserAuth.builder().email(user.getUsername()).nickname(user.getUsername()).avatar(systemConfigService.selectDefaultRegisterAvatar()).intro("介绍下你自己吧").webSite("http://refrainblog.cn").build();
        int insert = userAuthMapper.insert(userAuth);
        if (insert > 0) {
            // 密码加密
            user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));
            user.setLoginType(StatusConstants.LOGIN_TYPE_EMAIL);
            user.setUserAuthId(userAuth.getId());
            // 默认是用户
            user.setRoleId(2L);

            // 初始化基本信息
            initUserInfo(user);
            user.setLastLoginTime(new Date());
            return save(user);
        }
        return false;
    }

    @Override
    public boolean registerUser6(WxmpUserDTO wxmpUserDTO) {
        UserAuth userAuth = UserAuth
                .builder()
                // 这个应该是用户自己的昵称
                .nickname(wxmpUserDTO.getNickname())
                // 用户自己的头像，无需默认值
                .avatar(wxmpUserDTO.getAvatar())
                // 用户个人介绍
                .intro(wxmpUserDTO.getIntro())
                .build();
        int insert = userAuthMapper.insert(userAuth);
        if (insert > 0) {
            User user = new User();
            user.setUsername(wxmpUserDTO.getUsername());
            user.setLoginType(StatusConstants.LOGIN_TYPE_WXMP);
            user.setUserAuthId(userAuth.getId());
            // 微信小程序角色无任何权限
            user.setRoleId(3L);
            // 初始化基本信息
            initUserInfo(user);
            user.setLastLoginTime(new Date());
            return save(user);
        }
        return false;
    }

    @Override
    public Map<String, Object> sendRegisterMailCode(String username, String mailUuid, String mailCode) {
        // 验证邮箱格式是否正确(参数已经校验过)
        // BusinessAssert.isTure(iMailUtil.verifyMail(username), "邮箱格式不正确");
        HashMap<String, Object> result = new HashMap<>();
        // mailUuid = 用邮箱生成的随机ID，若重复生成则之前的失效
        // mailCode = random.IntNumber(6)随机6位数字
        // 正常情况下 mailUuid 和 mailCode 不为空，否则就是数据不完整
        String uuid = DigestUtil.md5Hex(username.getBytes());
        // 当验证邮箱码不和上传的 mailUuid 相同时直接重新发送邮箱
        if (uuid.equals(mailUuid)) {
            // 1、两个数据完整时，进行验证码验证
            if (StrUtil.isNotBlank(mailCode)) {
                // 从缓存中读取邮件对应的验证码
                BusinessAssert.isTure(redisCache.hasKey(getMailCodeKey(uuid)), "数据不存在，请重新发送验证码");
                String verifyCode = (String) redisCache.get(getMailCodeKey(uuid));
                // 如果验证码错误则返回错误
                BusinessAssert.isFalse(!mailCode.equals(verifyCode), "验证码错误，请重新输入");
                // 验证成功则删除缓存数据
                redisCache.del(getMailCodeKey(uuid));
                // 若验证通过则返回空结果，表示验证成功
                return null;
            }
            // 2、数据不完整时，要考虑没有给用户发送验证码或者重复发起请求
            if (redisCache.hasKey(getMailCodeKey(uuid))) {
                // 发了邮件所以 60s 内不能再次发送邮件，这里给值设置5分钟过期时间，若有值则查询有效时间是否小于240s
                long expireTime = redisCache.getExpire(getMailCodeKey(uuid));
                // 若有效时间大于240，则60s内不能重复发送邮件
                if (expireTime > 240L) {
                    // 返回 60s 倒计时时间
                    result.put("countdown", expireTime - 240L);
                    return result;
                }
            }
        }
        // 若过了 60s 倒计时或没有发邮件则需要发送邮件
        String code = RandomUtil.randomNumbers(6);
        MailEntity mailEntity = MailEntity.builder().sendTo(username).subject("注册验证码").text(iMailUtil.getRegisterMailCode(username, code, 5)).build();
        iMailUtil.sendHtmlEMail(mailEntity);
        // 缓存中记录邮件数据
        redisCache.set(getMailCodeKey(uuid), code, 300L);
        result.put("countdown", 60L);
        result.put("mailUuid", uuid);
        return result;
    }

    @Override
    public void sendRegisterMailCode(String username) {
        String uuid = DigestUtil.md5Hex(username.getBytes());
        if (redisCache.hasKey(getMailCodeKey(uuid))) {
            long expireTime = redisCache.getExpire(getMailCodeKey(uuid));
            // 若有效时间大于240，则60s内不能重复发送邮件
            if (expireTime > 240L) {
                // 重复发送邮件，过段时间再操作
                BusinessAssert.fail("邮件发送繁忙，请稍后再试");
            }
            // 否则重新发送验证码
        }
        // 若没有发邮件则需要发送邮件
        // 随机验证码
        String code = RandomUtil.randomNumbers(6);
        int timeout = 5;
        MailEntity mailEntity = MailEntity.builder().sendTo(username).subject("注册验证码").text(iMailUtil.getRegisterMailCode(username, code, timeout)).build();
        iMailUtil.sendHtmlEMail(mailEntity);
        // 缓存中记录邮件数据，默认时间 5 分钟
        redisCache.set(getMailCodeKey(uuid), code, 60 * timeout);
    }

    @Override
    public void validateMailCode(String username, String mailCode) {
        String uuid = DigestUtil.md5Hex(username.getBytes());
        if (!redisCache.hasKey(getMailCodeKey(uuid))) {
            // 重复发送邮件，过段时间再操作
            BusinessAssert.fail("注册失败，请重新发送验证码");
        }
        String verifyCode = (String) redisCache.get(getMailCodeKey(uuid));
        // 如果验证码错误则返回错误
        BusinessAssert.isFalse(!mailCode.equals(verifyCode), "验证码错误，请重新输入");
        // 验证成功则删除缓存数据
        redisCache.del(getMailCodeKey(uuid));
    }

    @Override
    public void validatePasswordMailCode(String username, String mailCode) {
        String uuid = DigestUtil.md5Hex(username.getBytes());
        if (!redisCache.hasKey(getPasswordMailCodeKey(uuid))) {
            // 重复发送邮件，过段时间再操作
            BusinessAssert.fail("操作失败，请重新发送验证码");
        }
        String verifyCode = (String) redisCache.get(getPasswordMailCodeKey(uuid));
        // 如果验证码错误则返回错误
        BusinessAssert.isFalse(!mailCode.equals(verifyCode), "验证码错误，请重新输入");
        // 验证成功则删除缓存数据
        redisCache.del(getPasswordMailCodeKey(uuid));
    }

    @Override
    public boolean alreadyRegister(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().eq(User::getUsername, username);
        // 用户名是唯一的，若已经注册则返回 true
        return getOne(queryWrapper) != null;
    }

    @Override
    public User userLogin(User user) {
        // SpringSecurity登录认证
        // 认证不通过时，SpringSecurity会主动抛出异常
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // // 更新登录用户信息
        getLoginUserInfo(loginUser);
        // // 为 loginUser 创建 token 并赋予 redis 缓存的唯一uuid键
        String token = iTokenService.createToken(loginUser);
        iSaveUserService.saveUserToken(loginUser.getUserUid());
        return loginUser.getUser();
    }

    @Override
    public Map<String, Object> getLoginUserInfo() {
        HashMap<String, Object> result = new HashMap<>();
        LoginUser loginUser = SecurityUtil.getLoginUser();
        // 用户角色信息
        String role = loginUser.getRole();
        result.put("role", role);
        // 用户权限信息
        Set<String> permissions = loginUser.getPermissions();
        result.put("permissions", permissions);
        // 用户名
        String username = loginUser.getUsername();
        result.put("username", username);
        // 用户头像
        Long userAuthId = loginUser.getUser().getUserAuthId();
        UserAuth userAuth = userAuthMapper.getByUserId(userAuthId);
        result.put("avatar", userAuth.getAvatar());
        return result;
    }

    /**
     * 通过微信接口服务登录凭证校验接口，获取微信小程序用户的openId等参数
     *
     * @param code 代码
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    private Map<String, Object> getWxmpParams(String code) {
        BusinessAssert.isFalse(StrUtil.isBlank(code), "用户凭证不能为空");
        // 获取用户 openId
        JSONObject resultJson = WxmpLoginUtil.getResultJson(code);
        BusinessAssert.isFalse(resultJson.isNull("openid"), "无此微信小程序用户，请输入正确的登录凭证");
        // 以 openid 和 sessionKey 生成 token返回
        String openid = resultJson.getStr("openid");
        String session_key = resultJson.getStr("session_key");
        Map<String, Object> params = new HashMap<>();
        params.put("openid", openid);
        params.put("session_key", session_key);
        return params;
    }


    @Override
    public String wxmpLogin(String code) {
        Map<String, Object> wxmpParams = getWxmpParams(code);
        String openid = wxmpParams.get("openid").toString();
        // 直接通过微信小程序登录的用户，无需密码，其openId就是用户账号
        // 使用自定义微信小程序登录身份检验器
        Authentication authenticate = authenticationManager.authenticate(new WxmpLoginAuthenticationToken(openid, null));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        getLoginUserInfo(loginUser);
        // 为 loginUser 手动赋值加后的 userUid
        loginUser.setUserUid(DigestUtil.md5Hex(openid));
        String token = iTokenService.createToken(loginUser, wxmpParams);
        iSaveUserService.saveUserToken(loginUser.getUserUid());
        return token;
    }

    @Override
    public boolean wxmpRegister(WxmpUserDTO wxmpUserDTO) {
        Map<String, Object> wxmpParams = getWxmpParams(wxmpUserDTO.getCode());
        String openid = wxmpParams.get("openid").toString();
        wxmpUserDTO.setUsername(openid);
        return registerUser6(wxmpUserDTO);
    }

    @Override
    public UserInfoVO wxmpUserInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        Long userAuthId = loginUser.getUser().getUserAuthId();
        UserAuth userAuth = userAuthMapper.selectById(userAuthId);
        BusinessAssert.notNull(userAuth, "用户信息不完整，请联系管理员");
        userAuth.setId(loginUser.getUser().getId());
        return BeanCopyUtil.copyBean(userAuth, UserInfoVO.class);
    }

    @Override
    public boolean wxmpModify(UserAuth userAuth) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        Long userAuthId = loginUser.getUser().getUserAuthId();
        UserAuth build = UserAuth.builder().id(userAuthId).nickname(userAuth.getNickname())
                // 因为头像可能是上传的base64格式，需要转化一下
                .avatar(userAuth.getAvatar()).intro(userAuth.getIntro()).webSite(userAuth.getWebSite()).email(userAuth.getEmail()).build();
        int update = userAuthMapper.updateById(build);
        return update > 0;
    }

    @Override
    public Map<String, Object> getUserProfile() {
        HashMap<String, Object> result = new HashMap<>();
        LoginUser loginUser = SecurityUtil.getLoginUser();
        // 用户登录方式
        result.put("loginType", loginUser.getUser().getLoginType());
        // 用户详细信息
        Long userAuthId = loginUser.getUser().getUserAuthId();
        UserAuth userAuth = userAuthMapper.getByUserId(userAuthId);
        result.put("user", userAuth);
        // 用户角色信息
        Long roleId = loginUser.getUser().getRoleId();
        SysRoleVO roleById = roleService.getRoleById(roleId);
        result.put("role", roleById.getName());
        return result;
    }

    /**
     * 微信小程序用户登录
     *
     * @param openId 用户唯一标识
     * @return {@link User}
     */
    private User getUserByOpenId(String openId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, openId);
        // 微信小程序登录，指定登录方式
        queryWrapper.eq(User::getLoginType, StatusConstants.LOGIN_TYPE_WXMP);
        return getOne(queryWrapper, false);
    }

    /**
     * 得到邮件代码关键key
     *
     * @param key 关键
     * @return {@link String}
     */
    private String getMailCodeKey(String key) {
        return CacheConstants.REGISTER_MAIL + key;
    }

    /**
     * 得到邮件代码关键key
     *
     * @param key 关键
     * @return {@link String}
     */
    private String getPasswordMailCodeKey(String key) {
        return CacheConstants.MODIFY_PASSWORD_MAIL + key;
    }

    /**
     * 获取登录用户信息
     *
     * @param loginUser 登录用户
     */
    public void getLoginUserInfo(LoginUser loginUser) {
        //修改登录信息
        initUserInfo(loginUser.getUser());
        threadPoolTaskExecutor.execute(() -> updateLoginInfo(loginUser.getUser()));
    }

    /**
     * 更新登录信息
     *
     * @param user 用户
     * @return boolean
     */
    public boolean updateLoginInfo(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getIpAddress, user.getIpAddress());
        updateWrapper.set(User::getIpSource, user.getIpSource());
        updateWrapper.set(User::getOs, user.getOs());
        updateWrapper.set(User::getBrowser, user.getBrowser());
        updateWrapper.set(User::getLastLoginTime, new Date());
        updateWrapper.eq(User::getId, user.getId());
        return update(updateWrapper);
    }

    /**
     * 初始化用户信息
     *
     * @param user 用户
     */
    private void initUserInfo(User user) {
        String ipAddress = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getCityInfo(ipAddress);
        UserAgent userAgent = IpUtil.getUserAgent(request);
        // 解析客户端操作系统类型
        String os = userAgent.getOs().getName();
        // 解析浏览器
        String browser = userAgent.getBrowser().getName();
        user.setIpAddress(ipAddress);
        user.setIpSource(ipSource);
        user.setOs(os);
        user.setBrowser(browser);
    }
}

