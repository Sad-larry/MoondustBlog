package work.moonzs.base.enums;

import work.moonzs.domain.entity.LoginUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 这个是个坑，我只是为了方便获取用户的角色信息而写的
 * 以后会换为一个缓存，存储这些数据量小的数据
 *
 * @author Moondust月尘
 */
public class UserRoleInfo {
    public static LoginUser user = null;

    private static final Map<Long, String> roleInfo = new HashMap<>();
    private static final List<Long> roles = new ArrayList<>();

    static {
        roleInfo.put(1L, "管理员");
        roleInfo.put(2L, "普通用户");
        roleInfo.put(3L, "测试的管理员");

        roles.add(1L);
        roles.add(2L);
        roles.add(3L);
    }

    public static String getRoleInfo(Long userid) {
        return roleInfo.get(userid);
    }

    public static boolean isExistRole(Long roleId) {
        return roles.contains(roleId);
    }
}
