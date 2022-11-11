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
@ApiModel(description = "字典DTO")
public class DictDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加字典时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "字典ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 字典名称
     */
    @ApiModelProperty(notes = "字典名称")
    @NotBlank(message = "字典名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 字典类型
     */
    @ApiModelProperty(notes = "字典类型")
    @NotBlank(message = "字典类型不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String type;
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
