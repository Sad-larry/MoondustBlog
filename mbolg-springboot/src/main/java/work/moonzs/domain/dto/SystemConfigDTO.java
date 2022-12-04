package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "系统配置DTO")
public class SystemConfigDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "{SystemConfigDTO.id.Null}", groups = VG.Insert.class)
    @NotNull(message = "{SystemConfigDTO.id.NotNull}", groups = VG.Update.class)
    private Long id;
    /**
     * 配置名称
     */
    @ApiModelProperty(notes = "配置名称")
    @NotBlank(message = "{SystemConfigDTO.configName.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Size(max = 100, message = "{SystemConfigDTO.configName.MaxSize}", groups = {VG.Insert.class, VG.Update.class})
    private String configName;
    /**
     * 配置键名
     */
    @ApiModelProperty(notes = "配置键名")
    @NotBlank(message = "{SystemConfigDTO.configKey.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Size(max = 100, message = "{SystemConfigDTO.configKey.MaxSize}", groups = {VG.Insert.class, VG.Update.class})
    private String configKey;
    /**
     * 配置键值
     */
    @ApiModelProperty(notes = "配置键值")
    @NotBlank(message = "{SystemConfigDTO.configValue.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Size(max = 500, message = "{SystemConfigDTO.configValue.MaxSize}", groups = {VG.Insert.class, VG.Update.class})
    private String configValue;
    /**
     * 系统内置(0否,1是)
     */
    @ApiModelProperty(notes = "系统内置(0否,1是)")
    @Range(message = "{SystemConfigDTO.configType.Range}", min = 0, max = 1, groups = {VG.Insert.class, VG.Update.class})
    private Integer configType;
    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    @Size(max = 500, message = "{SystemConfigDTO.remark.MaxSize}", groups = {VG.Insert.class, VG.Update.class})
    private String remark;
}

