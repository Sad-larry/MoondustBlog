package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "友链DTO")
public class FriendLinkDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加友链时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "友链ID不能为空", groups = {VG.Update.class, Default.class})
    private Long id;
    /**
     * 网站名称
     */
    @ApiModelProperty(notes = "网站名称")
    @NotBlank(message = "网站名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    @Length(message = "网站名称长度在1到20个字符", min = 1, max = 20, groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 网站地址
     */
    @ApiModelProperty(notes = "网站地址")
    @NotBlank(message = "网站地址不能为空", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "网站地址无效", groups = {VG.Insert.class, VG.Update.class})
    private String url;
    /**
     * 网站头像地址
     */
    @ApiModelProperty(notes = "网站头像地址")
    @NotBlank(message = "网站头像地址不能为空", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "网站头像地址无效", groups = {VG.Insert.class, VG.Update.class})
    private String avatar;
    /**
     * 网站描述
     */
    @ApiModelProperty(notes = "网站描述")
    @Length(message = "网站描述不宜太长", max = 512, groups = {VG.Insert.class, VG.Update.class})
    private String info;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    @NotBlank(message = "邮箱不能为空", groups = {VG.Insert.class, VG.Update.class})
    @Email(message = "邮箱无效", groups = {VG.Insert.class, VG.Update.class})
    private String email;
    /**
     * 排序
     */
    @ApiModelProperty(notes = "排序")
    private Integer sort;
    /**
     * ENUM-状态(0待审核,1通过)
     */
    @ApiModelProperty(notes = "ENUM-状态(0待审核,1通过)")
    @Null(message = "添加友链时状态必须为空", groups = VG.Insert.class)
    @NotNull(message = "友链状态不能为空", groups = VG.Update.class)
    @Range(min = 0, max = 1, groups = VG.Update.class)
    private Integer status;
}
