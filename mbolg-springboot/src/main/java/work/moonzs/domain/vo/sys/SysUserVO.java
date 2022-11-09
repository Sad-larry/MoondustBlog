package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 登录方式
     */
    private Integer loginType;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 用户状态(0禁用,1正常)
     */
    private Integer status;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
