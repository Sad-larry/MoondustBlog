package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "菜单DTO")
public class MenuDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @NotNull(message = "菜单ID不能为空", groups = {VG.Insert.class, VG.Update.class})
    private Long id;
    /**
     * 上级菜单ID
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String title;
    /**
     * 跳转地址
     */
    @ApiModelProperty(notes = "跳转地址")
    private String name;
    /**
     * 路由地址
     */
    @ApiModelProperty(notes = "路由地址")
    @NotBlank(message = "路由地址不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String url;
    /**
     * 菜单组件
     */
    @ApiModelProperty(notes = "菜单组件")
    @NotBlank(message = "菜单组件不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String component;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单类型(M菜单,F按钮)
     */
    @Pattern(regexp = "^[MF]$", message = "菜单类型只能为M、F", groups = {VG.Insert.class, VG.Update.class})
    private String type;
    /**
     * 菜单级别
     */
    @Min(value = 0, message = "菜单级别最小为0", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 2, message = "菜单级别最大为2", groups = {VG.Insert.class, VG.Update.class})
    private Integer level;
    /**
     * 显示顺序
     */
    @Min(value = 0, message = "排序字段只能为自然数", groups = {VG.Insert.class, VG.Update.class})
    private Integer sortNo;
    /**
     * 重定向地址
     */
    private String redirect;
    /**
     * 是否隐藏(0否,1是)
     */
    private Integer hidden;
    /**
     * 是否缓存（0不缓存,1缓存）
     */
    private Integer isCache;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
}
