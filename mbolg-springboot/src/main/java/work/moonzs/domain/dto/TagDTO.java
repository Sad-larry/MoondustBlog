package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

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
    @Null(message = "添加标签时id必须为NULL", groups = VG.Insert.class)
    @Min(message = "id值最小为1", value = 1L, groups = {VG.Select.class, VG.Delete.class, VG.Update.class})
    private Long id;
    /**
     * 标签名
     */
    @ApiModelProperty(notes = "标签名")
    @NotBlank(message = "标签'名'不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String tagName;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "标签描述")
    private String description;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "标签状态")
    @Pattern(regexp = "^[01]$", message = "设置标签状态只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private String status;
}
