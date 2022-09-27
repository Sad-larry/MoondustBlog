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
@ApiModel(description = "登录用户DTO")
public class LoginUserDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "用户密码")
    private String password;
}


