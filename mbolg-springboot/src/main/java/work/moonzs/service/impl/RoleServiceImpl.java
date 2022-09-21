package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Role;
import work.moonzs.mapper.RoleMapper;
import work.moonzs.service.RoleService;

/**
 * (Role)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}

