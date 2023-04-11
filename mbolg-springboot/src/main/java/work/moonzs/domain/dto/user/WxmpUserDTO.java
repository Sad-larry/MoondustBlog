package work.moonzs.domain.dto.user;

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
@ApiModel(description = "微信用户用户DTO")
public class WxmpUserDTO {
    /**
     * 登录凭证
     */
    @ApiModelProperty(notes = "code")
    @NotBlank(message = "登录凭证不能为空")
    private String code;
    /**
     * 用户名，相当于openid
     */
    @ApiModelProperty(notes = "username")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(notes = "昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;
    /**
     * 用户头像
     */
    @ApiModelProperty(notes = "用户头像")
    @NotBlank(message = "用户头像不能为空")
    private String avatar;
    /**
     * 个人简介
     */
    @ApiModelProperty(notes = "个人简介")
    private String intro;
}
