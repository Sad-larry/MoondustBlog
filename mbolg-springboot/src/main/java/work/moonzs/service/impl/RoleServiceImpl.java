package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysRoleVO;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.service.RoleService;

import java.util.List;

/**
 * 角色表(Role)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public PageVO<SysRoleVO> listRole(Integer pageNum, Integer pageSize, String name) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(name)) {
            page(page, new LambdaQueryWrapper<Role>().like(Role::getName, name));
        } else {
            page(page);
        }
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysRoleVO.class), page.getTotal());
    }

    @Override
    public boolean isExistRoleById(Long roleId) {
        return ObjUtil.isNotNull(getById(roleId));
    }

    @Override
    public boolean isExistRoleByName(String name) {
        return count(new LambdaQueryWrapper<Role>().eq(Role::getName, name)) > 0;
    }

    @Override
    public boolean isExistSameRoleByName(String name) {
        return count(new LambdaQueryWrapper<Role>().eq(Role::getName, name)) > 1;
    }

    @Override
    public SysRoleVO getRoleById(Long roleId) {
        return BeanCopyUtil.copyBean(getById(roleId), SysRoleVO.class);
    }

    @Override
    public Long insertRole(Role role) {
        BusinessAssert.isFalse(isExistRoleByName(role.getName()), AppHttpCodeEnum.ROLE_EXIST);
        baseMapper.insert(role);
        return role.getId();
    }

    @Override
    public boolean updateRole(Role role) {
        BusinessAssert.isTure(isExistRoleById(role.getId()), AppHttpCodeEnum.ROLE_NOT_EXIST);
        updateById(role);
        BusinessAssert.isFalse(isExistSameRoleByName(role.getName()), AppHttpCodeEnum.ROLE_EXIST);
        return true;
    }

    @Override
    public boolean deleteRole(Long[] roleIds) {
        return removeBatchByIds(List.of(roleIds));
    }
}

