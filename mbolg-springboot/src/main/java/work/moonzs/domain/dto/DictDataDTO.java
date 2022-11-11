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
@ApiModel(description = "字典数据DTO")
public class DictDataDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加字典数据时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "字典数据ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 字典类型ID
     */
    @ApiModelProperty(notes = "字典类型ID")
    @NotNull(message = "字典数据ID不能为空", groups = {VG.Insert.class, VG.Update.class})
    private Long dictId;
    /**
     * 字典标签
     */
    @ApiModelProperty(notes = "字典标签")
    @NotBlank(message = "字典标签不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String label;
    /**
     * 字典键值
     */
    @ApiModelProperty(notes = "字典键值")
    @NotBlank(message = "字典键值不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String value;
    /**
     * 回显样式
     */
    @ApiModelProperty(notes = "回显样式")
    private String style;
    /**
     * 是否默认(0否,1是)
     */
    @ApiModelProperty(notes = "是否默认(0否,1是)")
    private Integer isDefault;
    /**
     * 是否发布(0否,1是)
     */
    @ApiModelProperty(notes = "是否发布(0否,1是)")
    private Integer isPublish;
    /**
     * 排序
     */
    @ApiModelProperty(notes = "排序")
    private Integer sort;
    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    private String remark;
}
