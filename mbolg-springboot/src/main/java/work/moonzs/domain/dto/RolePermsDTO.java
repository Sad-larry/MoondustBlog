package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色权限DTO")
public class RolePermsDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @NotNull(message = "{RolePermsDTO.id.NotNull}")
    private Long id;
    /**
     * 角色编码
     */
    @ApiModelProperty(notes = "角色编码")
    private Long[] perms;
}
