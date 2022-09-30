package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Role;

/**
 * (Role)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface RoleService extends IService<Role> {

    /**
     * 角色列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> listRoles(Integer pageNum, Integer pageSize);

    /**
     * 通过角色id判断是否存在
     *
     * @param roleId 角色id
     * @return boolean
     */
    boolean isExistRoleById(Long roleId);
}

