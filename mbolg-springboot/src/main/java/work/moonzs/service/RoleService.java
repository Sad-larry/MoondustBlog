package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysRoleVO;

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
     * @param name     角色名
     * @return {@link PageVO}<{@link SysRoleVO}>
     */
    PageVO<SysRoleVO> listRole(Integer pageNum, Integer pageSize, String name);

    /**
     * 通过角色id判断是否存在
     *
     * @param roleId 角色id
     * @return boolean
     */
    boolean isExistRoleById(Long roleId);

    /**
     * 通过角色名判断是存在角色
     *
     * @param name 名字
     * @return boolean
     */
    boolean isExistRoleByName(String name);

    /**
     * 通过角色名判断是存在相同角色
     *
     * @param name 名字
     * @return boolean
     */
    boolean isExistSameRoleByName(String name);

    /**
     * 通过id查询角色详细信息
     *
     * @param roleId 角色id
     * @return {@link SysRoleVO}
     */
    SysRoleVO getRoleById(Long roleId);

    /**
     * 插入角色
     *
     * @param role 角色
     * @return {@link Long}
     */
    @Transactional
    Long insertRole(Role role);

    /**
     * 更新角色
     *
     * @param role 角色
     * @return boolean
     */
    @Transactional
    boolean updateRole(Role role);

    /**
     * 删除角色
     *
     * @param roleIds 角色id
     * @return boolean
     */
    @Transactional
    boolean deleteRole(Long[] roleIds);
}

