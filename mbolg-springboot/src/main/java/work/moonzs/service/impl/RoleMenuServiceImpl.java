package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.RoleMenu;
import work.moonzs.mapper.RoleMenuMapper;
import work.moonzs.service.RoleMenuService;

/**
 * (RoleMenu)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

