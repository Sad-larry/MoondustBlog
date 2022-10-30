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
     * 电子邮箱
     */
    @ApiModelProperty(notes = "电子邮箱")
    private String email;
    /**
     * 昵称
     */
    @ApiModelProperty(notes = "昵称")
    private String nickname;
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
     * 个人网站
     */
    @ApiModelProperty(notes = "个人网站")
    private String webSite;
}
