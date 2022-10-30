package work.moonzs.domain.entity;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.utils.BeanCopyUtil;

import java.util.Date;

/**
 * 在线用户显示
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {
    // id
    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickName;
    //手机号
    private String mobile;
    //电子邮箱
    private String email;
    //用户头像
    private String avatar;
    //生日
    private Date birthday;

    public static OnlineUser create(UserAuth user) {
        OnlineUser onlineUser = BeanCopyUtil.copyBean(user, OnlineUser.class);
        // 脱敏
        // onlineUser.setMobile(DesensitizedUtil.mobilePhone(user.getMobile()));
        onlineUser.setEmail(DesensitizedUtil.email(user.getEmail()));
        return onlineUser;
    }
}
