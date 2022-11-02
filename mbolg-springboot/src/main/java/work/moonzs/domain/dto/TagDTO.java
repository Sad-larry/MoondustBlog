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
@ApiModel(description = "标签DTO")
public class TagDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @Null(message = "标签ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "标签ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 标签名称
     */
    @ApiModelProperty(notes = "标签名")
    @NotBlank(message = "标签名不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(notes = "标签排序")
    private Integer sort;
}
