package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.moonzs.domain.entity.Menu;

import java.util.List;

/**
 * 权限资源表 (Menu)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:33:21
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

    /**
     * 通过用户id查询指定的菜单
     *
     * @param userId 用户id
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 通过用户id查询用户权限
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> selectUserPerms(Long userId);
}

