package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.UserRole;

/**
 * 用户角色关联表(UserRole)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:37:24
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 更新用户id
     *
     * @param roleId 角色id
     * @param userId 用户id
     */
    void updateByUserId(Long userId, Long roleId);
}

