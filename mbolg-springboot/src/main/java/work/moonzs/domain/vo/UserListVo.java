package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserListVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 账号状态(1正常,0停用)
     */
    private String status;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 角色
     */
    private String roles;
}
