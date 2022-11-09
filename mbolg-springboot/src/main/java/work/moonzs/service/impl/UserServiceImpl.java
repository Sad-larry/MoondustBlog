package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.base.web.service.ITokenService;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;
import work.moonzs.mapper.UserAuthMapper;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.UserService;

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

    @Override
    public String adminLogin(String username, String password, String uuid, String code) {
        // TODO 验证验证码是否正确
        // validateCaptcha(uuid, code);
        // SpringSecurity登录认证
        // 认证不通过时，SpringSecurity会主动抛出异常
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        return iTokenService.createToken(loginUser);
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
    }

    @Override
    public PageVO<SysUserVO> listUser(Integer pageNum, Integer pageSize, String username, Integer loginType) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<SysUserVO> userVOList = baseMapper.listUserPage(page, username, loginType);
        return new PageVO<>(userVOList, page);
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
}

