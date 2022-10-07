package work.moonzs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import work.moonzs.base.exception.AppHttpCodeException;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.User;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.mapper.UserMapper;

/**
 * @author Moondust月尘
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * SpringSecurity通过用户用户名查询数据库
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户名没有发现异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        queryWrapper.eq(User::getStatus, StatusConstants.NORMAL);
        User oneUser = userMapper.selectOne(queryWrapper);
        // 判断是否查到用户，否则抛出异常
        if (ObjectUtil.isNull(oneUser)) {
            // 返回null会抛出“用户不存在异常，在认证处理器接收
            return null;
        }
        // 返回登录用户
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(oneUser);
        return loginUser;
    }
}
