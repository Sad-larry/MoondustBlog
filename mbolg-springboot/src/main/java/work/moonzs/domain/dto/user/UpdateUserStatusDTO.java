package work.moonzs.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "修改用户DTO")
public class UpdateUserStatusDTO {
    /**
     * 主键
     */
    @ApiModelProperty(notes = "主键")
    @NotNull(message = "{UpdateUserStatusDTO.id.NotNull}")
    private Long id;
    /**
     * 角色ID
     */
    @ApiModelProperty(notes = "角色ID")
    @NotNull(message = "{UpdateUserStatusDTO.id.NotNull}")
    private Long roleId;
    /**
     * 用户状态(0禁用,1正常)
     */
    @ApiModelProperty(notes = "用户状态(0禁用,1正常)")
    @NotNull(message = "{UpdateUserStatusDTO.id.NotNull}")
    @Range(message = "{UpdateUserStatusDTO.status.Range}", min = 0, max = 1)
    private Integer status;
}
