package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Menu;

import java.util.List;

/**
 * (Menu)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 通过用户id查询该用户的角色信息，
     * 再根据角色查询角色的菜单信息
     *
     * @param userId 用户id
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> selectUserMenus(Long userId);
}

