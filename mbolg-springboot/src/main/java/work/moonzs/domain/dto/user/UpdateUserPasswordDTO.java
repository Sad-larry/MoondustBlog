package work.moonzs.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.NotBlank;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户DTO")
public class UpdateUserPasswordDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    @NotBlank(message = "{UpdateUserPasswordDTO.username.NotBlank}", groups = {VG.Select.class})
    private String username;
    /**
     * 旧密码
     */
    @ApiModelProperty(notes = "新密码")
    @NotBlank(message = "{UpdateUserPasswordDTO.oldPassword.NotBlank}", groups = {VG.Update.class})
    private String oldPassword;
    /**
     * 新密码
     */
    @ApiModelProperty(notes = "新密码")
    @NotBlank(message = "{UpdateUserPasswordDTO.newPassword.NotBlank}", groups = {VG.Select.class, VG.Update.class})
    private String newPassword;
    /**
     * 邮箱验证码
     */
    @ApiModelProperty(notes = "邮箱验证码")
    @NotBlank(message = "{UpdateUserPasswordDTO.mailCode.NotBlank}", groups = {VG.Select.class})
    private String mailCode;
}
