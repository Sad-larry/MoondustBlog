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
@ApiModel(description = "角色DTO")
public class RoleDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @Null(message = "添加角色时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "角色ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 角色编码
     */
    @ApiModelProperty(notes = "角色编码")
    @NotBlank(message = "角色编码不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String code;
    /**
     * 角色名
     */
    @ApiModelProperty(notes = "角色名")
    @NotBlank(message = "角色名不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 角色描述
     */
    @ApiModelProperty(notes = "角色描述")
    private String remark;
}
