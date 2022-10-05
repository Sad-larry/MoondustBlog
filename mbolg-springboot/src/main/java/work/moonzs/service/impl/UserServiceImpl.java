package work.moonzs.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.MenuTreeVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.UserInfoVo;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.enums.StatusConstants;
import work.moonzs.enums.UserRoleInfo;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.UserService;
import work.moonzs.utils.BeanCopyUtils;

import java.util.*;
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
        //  查询用户所具有的角色，每个用户都有一个角色
        // 通过角色id查询菜单id列表
        // 查询出所有的菜单，一一匹配存入到列表中
        // 对菜单列表进行整理
        List<Menu> menus = userMapper.selectUserMenus(oneUser.getId());
        List<MenuTreeVo> menuTree = organizeMenu(menus);
        // 组装数据返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userInfo", userInfo);
        map.put("menuTree", menuTree);
        return ResponseResult.success(map);
    }

    /**
     * 用户列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> listUsers(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(User::getUserName, fuzzyField);
            queryWrapper.or().like(User::getNickName, fuzzyField);
        }
        queryWrapper.eq(User::getStatus, StatusConstants.NORMAL);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<User> list = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(list, UserListVo.class);
        // TODO 设置每个用户的角色信息
        userListVos.forEach(userListVo -> userListVo.setRoles(UserRoleInfo.getRoleInfo(userListVo.getId())));
        PageVo<UserListVo> pageVo = new PageVo<>(userListVos, page.getTotal(), page.getCurrent(), page.getSize());
        return ResponseResult.success(pageVo);
    }

    /**
     * 保存用户
     *
     * @param user 用户
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public Long saveUser(User user) {
        user.setPassword(user.getPassword());
        // 保存用户返回用户id，并添加角色
        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 组织菜单
     * 以树形结构返回
     *
     * @param menus 菜单
     * @return {@link List}<{@link MenuTreeVo}>
     */
    private List<MenuTreeVo> organizeMenu(List<Menu> menus) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setChildrenKey("children");
        // 最大递归深度
        treeNodeConfig.setDeep(2);

        List<Tree<Long>> build = TreeUtil.build(menus, 0L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            // tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
            // 扩展属性 ...
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("icon", treeNode.getIcon());
        });
        return build.stream().map(longTree -> {
            // 将Tree<Long>对象转化为JSON字符串
            String s = JSONUtil.toJsonStr(longTree);
            // 然后把JSON字符串转化为MenuTreeVo对象
            return JSONUtil.toBean(s, MenuTreeVo.class);
        }).collect(Collectors.toList());
    }

    /**
     * 整理菜单列表
     * tree类型
     *
     * @param menus 菜单列表
     * @return {@link MenuTreeVo}
     */
    @Deprecated
    private List<MenuTreeVo> finishingMenu(List<Menu> menus) {
        // 菜单列表以id排好序，虽然应该是正序的
        List<Menu> collectMenus = menus.stream().sorted(Comparator.comparing(Menu::getId)).toList();
        List<MenuTreeVo> menuTree = new ArrayList<>();
        for (Menu menu : collectMenus) {
            // 将每个菜单转化为MenuTreeVo对象
            MenuTreeVo menuTreeVo = BeanCopyUtils.copyBean(menu, MenuTreeVo.class);
            // 如果这个menu的pid为0，说明他是一级菜单，直接添加到返回列表
            // 否则获取列表的最后一个菜单添加到子菜单中
            if (menu.getPid() == 0) {
                menuTree.add(menuTreeVo);
            } else {
                menuTree.get(menuTree.size() - 1).getChildren().add(menuTreeVo);
            }
        }
        return menuTree;
    }
}

