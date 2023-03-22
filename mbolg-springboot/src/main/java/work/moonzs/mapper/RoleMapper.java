package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.sys.SysPermissionVO;

import java.util.List;

/**
 * 角色表(Role)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:34:09
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过用户id查询该用户的角色信息，
     *
     * @param userId 用户id
     * @return {@link String}
     */
    String selectUserRole(Long userId);

    /**
     * 通过角色id查询权限
     *
     * @param roleId 角色id
     * @return {@link List}<{@link SysPermissionVO}>
     */
    List<SysPermissionVO> queryByRoleId(Long roleId);
}

