package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.UserRole;

/**
 * (UserRole)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
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

