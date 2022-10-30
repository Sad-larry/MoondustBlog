package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.RoleVo;

/**
 * 角色表(Role)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:45
 */
public interface RoleService extends IService<Role> {

    /**
     * 角色列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVo}<{@link RoleVo}>
     */
    PageVo<RoleVo> listRoles(Integer pageNum, Integer pageSize);

    /**
     * 通过角色id判断是否存在
     *
     * @param roleId 角色id
     * @return boolean
     */
    boolean isExistRoleById(Long roleId);
}

