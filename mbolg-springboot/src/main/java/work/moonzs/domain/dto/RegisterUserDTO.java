package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户注册时，只能有特定的几个字段
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户注册DTO")
public class RegisterUserDTO {
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    /**
     * 登录密码
     */
    @ApiModelProperty(notes = "登录密码")
    @NotBlank(message = "登录密码不能为空")
    private String password;
    /**
     * 登录方式
     */
    @ApiModelProperty(notes = "登录方式")
    @NotNull(message = "登录方式不能为空")
    private Integer loginType;
}
