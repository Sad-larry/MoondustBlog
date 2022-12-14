package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.UserRole;
import work.moonzs.mapper.UserRoleMapper;
import work.moonzs.service.UserRoleService;

/**
 * 用户角色关联表(UserRole)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:33
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public void updateByUserId(Long userId, Long roleId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        update(new UserRole(null, userId, roleId), queryWrapper);
    }
}

