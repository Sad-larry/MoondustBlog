package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息DTO")
public class UserAuthDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(notes = "用户ID")
    @Null(message = "{UserAuthDTO.id.Null}")
    private Long id;
    /**
     * 邮箱号
     */
    @ApiModelProperty(notes = "邮箱号")
    private String email;
    /**
     * 用户昵称
     */
    @ApiModelProperty(notes = "用户昵称")
    @NotBlank(message = "{UserAuthDTO.nickname.NotBlank}")
    private String nickname;
    /**
     * 用户头像
     */
    @ApiModelProperty(notes = "用户头像")
    @NotBlank(message = "{UserAuthDTO.avatar.NotBlank}")
    private String avatar;
    /**
     * 用户简介
     */
    @ApiModelProperty(notes = "用户简介")
    private String intro;
    /**
     * 个人网站
     */
    @ApiModelProperty(notes = "个人网站")
    private String webSite;
}

