package work.moonzs.service.impl;

import cn.hutool.core.util.ObjectUtil;
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
import work.moonzs.base.exception.ServiceException;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.JwtUtil;
import work.moonzs.base.utils.RedisUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.UserService;

import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String adminLogin(String username, String password, String uuid, String code) {
        // TODO 验证验证码是否正确
        // validateCaptcha(uuid, code);
        // SpringSecurity登录认证
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        // 认证没通过 authenticate为空
        if (ObjectUtil.isNull(authenticate)) {
            throw new ServiceException(AppHttpCodeEnum.USER_FAILED_CERTIFICATION);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = String.valueOf(loginUser.getUserId());
        // jwt生成token，利用用户id作为主题
        // TODO 如果用户选了remember me的话，要把token时长设为7天
        String token = JwtUtil.createJWT(userId);
        // 把token存入redis
        redisUtil.set(CacheConstants.TOKEN_KEY + userId, token);
        // 将用户信息存入redis
        redisUtil.set(CacheConstants.LOGIN_USER_KEY + userId, loginUser);
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
        String captcha = (String) redisUtil.get(verifyKey);
        redisUtil.del(verifyKey);
        if (captcha == null) {
            throw new ServiceException(AppHttpCodeEnum.CAPTCHA_FAIL);
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException(AppHttpCodeEnum.CAPTCHA_FAIL);
        }
    }

    @Override
    public void adminLogout() {
        Long userId = SecurityUtil.getUserId();
        // 从redis中删除用户
        redisUtil.del(CacheConstants.TOKEN_KEY + userId);
    }

    @Override
    public PageVo<UserListVo> listUsers(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(User::getUserName, fuzzyField);
            queryWrapper.or().like(User::getNickName, fuzzyField);
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

