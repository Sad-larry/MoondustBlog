package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册时，只能有特定的几个字段
 * 但是用户注册只有用邮箱注册，所以登录方式不需要设置
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
    @NotBlank(message = "{RegisterUserDTO.username.NotBlank}")
    private String username;
    /**
     * 登录密码
     */
    @ApiModelProperty(notes = "登录密码")
    @NotBlank(message = "{RegisterUserDTO.password.NotBlank}")
    private String password;
    /**
     * 邮件注册验证码
     */
    @ApiModelProperty(notes = "邮件注册验证码")
    @NotBlank(message = "{RegisterUserDTO.mailCode.NotBlank}")
    private String mailCode;
}
