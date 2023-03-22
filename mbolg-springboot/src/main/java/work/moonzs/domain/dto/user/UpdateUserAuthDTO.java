package work.moonzs.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户信息DTO")
public class UpdateUserAuthDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(notes = "用户ID")
    @NotNull(message = "{UpdateUserAuthDTO.id.NotNull}")
    private Long id;
    /**
     * 用户昵称
     */
    @ApiModelProperty(notes = "用户昵称")
    @NotBlank(message = "{UpdateUserAuthDTO.nickname.NotBlank}")
    private String nickname;
    /**
     * 用户简介
     */
    @ApiModelProperty(notes = "用户简介")
    @NotBlank(message = "{UpdateUserAuthDTO.intro.NotBlank}")
    private String intro;
}

