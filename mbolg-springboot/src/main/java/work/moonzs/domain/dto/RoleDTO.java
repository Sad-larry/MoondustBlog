package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色DTO")
public class RoleDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 角色名
     */
    @ApiModelProperty(notes = "角色名")
    private String roleName;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "描述")
    private String description;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "角色状态")
    private String status;
    /**
     * 菜单id
     */
    @ApiModelProperty(notes = "菜单列表")
    private List<Long> menuIds;
    /**
     * 资源id
     */
    @ApiModelProperty(notes = "资源列表")
    private List<Long> resourceIds;
}
