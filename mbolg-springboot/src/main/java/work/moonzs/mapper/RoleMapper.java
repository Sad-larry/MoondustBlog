package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Role;

/**
 * (Role)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过用户id查询该用户的角色信息，
     *
     * @param userId 用户id
     * @return {@link Role}
     */
    Role selectUserRole(Long userId);
}

