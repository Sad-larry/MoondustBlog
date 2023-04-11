package work.moonzs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.mapper.UserMapper;

import java.util.Collections;

/**
 * @author Moondust月尘
 */
@Service("UserDetailsServiceImplForWxmpLogin")
public class UserDetailsServiceImplForWxmpLogin implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * SpringSecurity通过微信小程序openId查询数据库
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户名没有发现异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.defineLoadUserByUsername(username);
        // 判断是否查到用户，否则抛出异常
        if (ObjectUtil.isNull(user)) {
            // 返回null会抛出“用户不存在异常，在认证处理器接收
            return null;
        }
        String role = this.getRoleByUserId(user.getId());
        // 返回登录用户
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUser(user);
        loginUser.setRole(role);
        loginUser.setPermissions(Collections.emptySet());
        return loginUser;
    }

    /**
     * 通过用户用户名查询一条数据
     *
     * @param username 用户名
     * @return {@link User}
     */
    public User defineLoadUserByUsername(String username) {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username).last("limit 1");
        queryWrapper.eq(User::getLoginType, StatusConstants.LOGIN_TYPE_WXMP);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 通过用户id查询角色
     *
     * @param userId 用户id
     * @return {@link String}
     */
    public String getRoleByUserId(Long userId) {
        if (SecurityUtil.isAdmin(userId)) {
            return "ROLE_admin";
        } else {
            return roleMapper.selectUserRole(userId);
        }
    }
}
