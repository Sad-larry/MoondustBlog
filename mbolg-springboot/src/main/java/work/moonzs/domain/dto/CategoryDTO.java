package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
    @Null(message = "分类ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "分类ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 分类名
     */
    @ApiModelProperty(notes = "分类名")
    @NotBlank(message = "分类名不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(notes = "分类排序")
    private Integer sort;
}
