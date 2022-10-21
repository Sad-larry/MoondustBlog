package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录用户DTO")
public class LoginUserDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    @NotBlank(message = "username不能为空")
    // @UsernameValid
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "用户密码")
    @NotBlank(message = "password不能为空")
    private String password;

    /**
     * 唯一标识id，存入redis的验证码的前缀
     */
    private String uuid;

    /**
     * 验证码
     */
    private String code;
}


