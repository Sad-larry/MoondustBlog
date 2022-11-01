package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.base.web.service.ITokenService;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.UserListVo;
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
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ITokenService iTokenService;

    @Override
    public String adminLogin(String username, String password, String uuid, String code) {
        // TODO 验证验证码是否正确
        validateCaptcha(uuid, code);
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
    public PageVo<UserListVo> listUsers(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(User::getUsername, fuzzyField);
        }
        queryWrapper.eq(User::getStatus, StatusConstants.NORMAL);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<User> list = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtil.copyBeanList(list, UserListVo.class);
        // TODO 设置每个用户的角色信息

        return new PageVo<>(userListVos, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public Long saveUser(User user) {
        user.setPassword(user.getPassword());
        // 保存用户返回用户id，并添加角色
        userMapper.insert(user);
        return user.getId();
    }
}

