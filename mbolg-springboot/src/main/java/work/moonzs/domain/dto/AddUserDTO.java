package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO article,comment,loginuser,user基本可以，其他的还需要添加删除或者修改
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加用户DTO")
public class AddUserDTO {
    /**
     * 主键
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(notes = "昵称")
    private String nickName;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码")
    private String password;
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
     * 生日
     */
    @ApiModelProperty(notes = "生日")
    private Date birthday;
    /**
     * 个人简介
     */
    @ApiModelProperty(notes = "个人简介")
    private String intro;
    /**
     * 角色id
     */
    @ApiModelProperty(notes = "角色id")
    private Long roleId;
}
