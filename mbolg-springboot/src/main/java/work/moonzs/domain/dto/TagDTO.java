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
@ApiModel(description = "标签DTO")
public class TagDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 标签名
     */
    @ApiModelProperty(notes = "标签名")
    private String tagName;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "标签描述")
    private String description;
}
