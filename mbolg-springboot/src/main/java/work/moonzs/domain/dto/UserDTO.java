package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户DTO")
public class UserDTO {
    /**
     * 主键
     */
    @ApiModelProperty(notes = "主键")
    @Null(message = "添加用户时id必须为NULL", groups = VG.Insert.class)
    @NotNull(message = "修改时id不能为NULL", groups = VG.Update.class)
    private Long id;
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    private String username;
    /**
     * 登录密码
     */
    @ApiModelProperty(notes = "登录密码")
    private String password;
    /**
     * 用户详情ID
     */
    @ApiModelProperty(notes = "用户详情ID")
    private Long userAuthId;
    /**
     * 角色ID
     */
    @ApiModelProperty(notes = "角色ID")
    private Long roleId;
    /**
     * 用户状态(0禁用,1正常)
     */
    @ApiModelProperty(notes = "用户状态(0禁用,1正常)")
    private Integer status;
    /**
     * ip地址
     */
    @ApiModelProperty(notes = "ip地址")
    private String ipAddress;
    /**
     * ip来源
     */
    @ApiModelProperty(notes = "ip来源")
    private String ipSource;
    /**
     * 登录系统
     */
    @ApiModelProperty(notes = "登录系统")
    private String os;
    /**
     * 登录方式
     */
    @ApiModelProperty(notes = "登录方式")
    private Integer loginType;
    /**
     * 浏览器
     */
    @ApiModelProperty(notes = "浏览器")
    private String browser;
    /**
     * 最后登录时间
     */
    @ApiModelProperty(notes = "最后登录时间")
    private Date lastLoginTime;
}
