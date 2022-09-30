package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 昵称
     */
    @ApiModelProperty(notes = "昵称")
    private String nickName;
    /**
     * 手机号
     */
    @ApiModelProperty(notes = "手机号")
    private String mobile;
    /**
     * 电子邮箱
     */
    @ApiModelProperty(notes = "电子邮箱")
    private String email;
    /**
     * 用户头像
     */
    @ApiModelProperty(notes = "用户头像")
    private String avatar;
    /**
     * 个人简介
     */
    @ApiModelProperty(notes = "个人简介")
    private String intro;
    /**
     * 账号状态(1正常,0停用)
     */
    @ApiModelProperty(notes = "账号状态(1正常,0停用)")
    private String status;
    /**
     * 角色id
     */
    @ApiModelProperty(notes = "角色id")
    private Long roleId;
}
