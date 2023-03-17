package work.moonzs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.mapper.MenuMapper;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.mapper.UserMapper;

import java.util.List;

/**
 * @author Moondust月尘
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * SpringSecurity通过用户用户名查询数据库
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
        // 获取用户权限信息
        // List<String> perms = this.getPermsByUserId(user.getId());
        String role = this.getRoleByUserId(user.getId());
        // 返回登录用户
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUser(user);
        loginUser.setRole(role);
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
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 通过用户id查询角色
     *
     * @param userId 用户id
     * @return {@link String}
     */
    public String getRoleByUserId(Long userId) {
        return roleMapper.selectUserRole(userId);
    }

    /**
     * 通过用户id查询用户权限
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    private List<String> getPermsByUserId(Long userId) {
        return menuMapper.selectUserPerms(userId);
    }
}
