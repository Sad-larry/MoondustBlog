package work.moonzs.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.enums.UserRoleInfo;
import work.moonzs.base.exception.ServiceException;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.JwtUtil;
import work.moonzs.base.utils.RedisUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.MenuTreeVo;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String adminLogin(String username, String password, String uuid, String code) {
        // 验证验证码是否正确
        validateCaptcha(uuid, code);
        // SpringSecurity登录认证
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        // 认证没通过 authenticate为空
        if (ObjectUtil.isNull(authenticate)) {
            throw new ServiceException(AppHttpCodeEnum.USER_FAILED_CERTIFICATION);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = String.valueOf(loginUser.getUserId());
        // jwt生成token，利用用户id作为主题
        // TODO 如果用户选了remember me的话，要把token时长设为7天
        String token = JwtUtil.createJWT(userId);
        // 把token存入redis
        redisUtil.set(CacheConstants.TOKEN_KEY + userId, token);
        // 将用户信息存入redis
        redisUtil.set(CacheConstants.LOGIN_USER_KEY + userId, loginUser);
        return token;
    }

    /**
     * 验证验证码
     *
     * @param uuid uuid
     * @param code 代码
     */
    private void validateCaptcha(String uuid, String code) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = (String) redisUtil.get(verifyKey);
        redisUtil.del(verifyKey);
        if (captcha == null) {
            throw new ServiceException(AppHttpCodeEnum.CAPTCHA_FAIL);
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException(AppHttpCodeEnum.CAPTCHA_FAIL);
        }
    }

    /**
     * 管理员注销
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> adminLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((LoginUser) authentication.getPrincipal()).getUserId().toString();
        // 从redis中删除用户
        redisUtil.del(CacheConstants.TOKEN_KEY + userId);
        return ResponseResult.success();
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
        List<UserListVo> userListVos = BeanCopyUtil.copyBeanList(list, UserListVo.class);
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
            tree.setName(treeNode.getMenuName());
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
            MenuTreeVo menuTreeVo = BeanCopyUtil.copyBean(menu, MenuTreeVo.class);
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

