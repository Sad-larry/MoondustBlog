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
@ApiModel(description = "菜单DTO")
public class MenuDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 菜单名称
     */
    @ApiModelProperty(notes = "菜单名称")
    private String menuName;
    /**
     * 路径
     */
    @ApiModelProperty(notes = "路径")
    private String path;
    /**
     * 组件
     */
    @ApiModelProperty(notes = "组件")
    private String component;
    /**
     * 图标
     */
    @ApiModelProperty(notes = "图标")
    private String icon;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "分类状态")
    private String status;
}
