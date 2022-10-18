package work.moonzs.controller.system;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.utils.BeanCopyUtils;
import work.moonzs.base.utils.SecurityUtils;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.LoginUserDTO;
import work.moonzs.domain.entity.Menu;
import work.moonzs.domain.entity.User;
import work.moonzs.service.MenuService;
import work.moonzs.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Moondust月尘
 */
@RestController("value = SystemLoginC")
@RequestMapping("/system")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    /**
     * 管理员登录
     *
     * @param loginUserDTO 登录用户dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping("/login")
    public ResponseResult<?> adminLogin(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        User user = BeanCopyUtils.copyBean(loginUserDTO, User.class);
        // SpringSecurity登录认证
        return userService.adminLogin(user);
    }

    /**
     * 管理员注销
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/logout")
    public ResponseResult<?> adminLogout() {
        return userService.adminLogout();
    }


    @GetMapping("/getRouters")
    public ResponseResult<?> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectMenuTreeByUserId(userId);
        // return ResponseResult.success(menuService.buildMenus(menus));
        return ResponseResult.success(menus);
    }


    /**
     * 德鲁伊统计
     *
     * @return {@link Object}
     */
    @GetMapping("/druid/stat")
    public Object druidStat() {
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }

}