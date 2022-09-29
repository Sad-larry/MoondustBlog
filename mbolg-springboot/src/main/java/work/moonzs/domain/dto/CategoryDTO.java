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
@ApiModel(description = "分类DTO")
public class CategoryDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 分类名
     */
    @ApiModelProperty(notes = "分类名")
    private String categoryName;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "分类描述")
    private String description;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "分类状态")
    private String status;
}
