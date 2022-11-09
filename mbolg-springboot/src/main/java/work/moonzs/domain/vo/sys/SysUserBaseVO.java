package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserBaseVO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 用户状态(0禁用,1正常)
     */
    private Integer status;
}
