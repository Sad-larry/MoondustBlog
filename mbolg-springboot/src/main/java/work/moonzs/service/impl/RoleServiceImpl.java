package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Role;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.RoleVo;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.service.RoleService;

import java.util.List;

/**
 * (Role)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public PageVo<RoleVo> listRoles(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, StatusConstants.NORMAL);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Role> list = page.getRecords();
        List<RoleVo> roleListVos = BeanCopyUtil.copyBeanList(list, RoleVo.class);
        return new PageVo<>(roleListVos, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public boolean isExistRoleById(Long roleId) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, roleId);
        queryWrapper.eq(Role::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count > 0;
    }
}

