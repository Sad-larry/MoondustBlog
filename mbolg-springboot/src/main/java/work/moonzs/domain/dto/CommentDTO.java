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
@ApiModel(description = "评论DTO")
public class CommentDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    private Long id;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "评论状态")
    private String status;
}
