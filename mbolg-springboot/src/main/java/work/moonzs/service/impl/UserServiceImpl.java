package work.moonzs.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.entity.RoleMenu;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserRole;
import work.moonzs.domain.vo.MenuListVo;
import work.moonzs.domain.vo.UserInfoVo;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.MenuService;
import work.moonzs.service.RoleMenuService;
import work.moonzs.service.UserRoleService;
import work.moonzs.service.UserService;
import work.moonzs.utils.BeanCopyUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (User)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 管理员登录
     *
     * @param user 用户
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> adminLogin(User user) {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());
        // TODO 暂时用这个
        queryWrapper.eq(User::getPassword, user.getPassword());
        User oneUser = getOne(queryWrapper);
        // 判断是否查到用户，否则返回失败
        if (Objects.isNull(oneUser)) {
            return ResponseResult.fail(AppHttpCodeEnum.USER_NOT_EXIST);
        }
        // TODO 判断密码是否正确
        // 用户信息
        UserInfoVo userInfo = BeanCopyUtils.copyBean(oneUser, UserInfoVo.class);
        // TODO jwt生成token
        String token = "待实现token";
        // 查询用户所具有的角色，每个用户都有一个角色
        // 通过角色id查询菜单id列表
        // 查询出所有的菜单，一一匹配存入到列表中
        // 对菜单列表进行整理
        List<Menu> menus = userMapper.selectUserMenus(oneUser.getId());
        List<MenuListVo> menuList = organizeMenu(menus);
        // 组装数据返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userInfo", userInfo);
        map.put("menuList", menuList);
        return ResponseResult.success(map);
    }

    /**
     * 组织菜单
     * 以树形结构返回
     *
     * @param menus 菜单
     * @return {@link List}<{@link MenuListVo}>
     */
    private List<MenuListVo> organizeMenu(List<Menu> menus) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setNameKey("menuName");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setChildrenKey("subMenuList");
        // 最大递归深度
        treeNodeConfig.setDeep(2);

        List<Tree<Long>> build = TreeUtil.build(menus, 0L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            // tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getMenuName());
            // 扩展属性 ...
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("icon", treeNode.getIcon());
        });
        return build.stream().map(longTree -> {
            // 将Tree<Long>对象转化为JSON字符串
            String s = JSONUtil.toJsonStr(longTree);
            // 然后把JSON字符串转化为MenuListVo对象
            return JSONUtil.toBean(s, MenuListVo.class);
        }).collect(Collectors.toList());
    }

    /**
     * 整理菜单列表
     * tree类型
     *
     * @param menus 菜单列表
     * @return {@link MenuListVo}
     */
    @Deprecated
    private List<MenuListVo> finishingMenu(List<Menu> menus) {
        // 菜单列表以id排好序，虽然应该是正序的
        List<Menu> collectMenus = menus.stream().sorted(Comparator.comparing(Menu::getId)).toList();
        List<MenuListVo> menuList = new ArrayList<>();
        for (Menu menu : collectMenus) {
            // 将每个菜单转化为MenuListVo对象
            MenuListVo menuListVo = BeanCopyUtils.copyBean(menu, MenuListVo.class);
            // 如果这个menu的pid为0，说明他是一级菜单，直接添加到返回列表
            // 否则获取列表的最后一个菜单添加到子菜单中
            if (menu.getPid() == 0) {
                menuListVo.setSubMenuList(new ArrayList<>());
                menuList.add(menuListVo);
            } else {
                menuList.get(menuList.size() - 1).getSubMenuList().add(menuListVo);
            }
        }
        return menuList;
    }
}

