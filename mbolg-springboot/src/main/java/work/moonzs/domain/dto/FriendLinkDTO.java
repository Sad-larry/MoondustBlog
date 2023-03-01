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
    @Null(message = "{FriendLinkDTO.id.Null}", groups = VG.Insert.class)
    @NotNull(message = "{FriendLinkDTO.id.NotNull}", groups = {VG.Update.class, Default.class})
    private Long id;
    /**
     * 网站名称
     */
    @ApiModelProperty(notes = "网站名称")
    @NotBlank(message = "{FriendLinkDTO.name.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Length(message = "{FriendLinkDTO.name.Length}", min = 1, max = 20, groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 网站地址
     */
    @ApiModelProperty(notes = "网站地址")
    @NotBlank(message = "{FriendLinkDTO.url.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "{FriendLinkDTO.url.URL}", groups = {VG.Insert.class, VG.Update.class})
    private String url;
    /**
     * 网站头像地址
     */
    @ApiModelProperty(notes = "网站头像地址")
    @NotBlank(message = "{FriendLinkDTO.avatar.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "{FriendLinkDTO.avatar.URL}", groups = {VG.Insert.class, VG.Update.class})
    private String avatar;
    /**
     * 网站描述
     */
    @ApiModelProperty(notes = "网站描述")
    @Length(message = "{FriendLinkDTO.info.Length}", max = 512, groups = {VG.Insert.class, VG.Update.class})
    private String info;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    @NotBlank(message = "{FriendLinkDTO.email.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Email(message = "{FriendLinkDTO.email.Email}", groups = {VG.Insert.class, VG.Update.class})
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
    @Null(message = "{FriendLinkDTO.status.Null}", groups = VG.Insert.class)
    @NotNull(message = "{FriendLinkDTO.status.NotNull}", groups = VG.Update.class)
    @Range(message = "{FriendLinkDTO.status.Range}", min = 0, max = 1, groups = VG.Update.class)
    private Integer status;
}
