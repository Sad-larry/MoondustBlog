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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "相册DTO")
public class PhotoAlbumDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加相册时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "相册ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 相册名
     */
    @ApiModelProperty(notes = "相册名")
    @NotBlank(message = "相册名不能为空", groups = {VG.Insert.class, VG.Update.class})
    @Length(message = "相册名长度在1到20个字符", min = 1, max = 20, groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 相册描述
     */
    @ApiModelProperty(notes = "相册描述")
    @Length(message = "相册描述不宜太长", max = 256, groups = {VG.Insert.class, VG.Update.class})
    private String info;
    /**
     * 相册封面
     */
    @ApiModelProperty(notes = "相册封面")
    @NotNull(message = "相册封面不能为空", groups = VG.Update.class)
    @URL(message = "相册封面链接无效", groups = {VG.Insert.class, VG.Update.class})
    private String cover;
    /**
     * 相册状态(0私密,1公开)
     */
    @ApiModelProperty(notes = "相册状态(0私密,1公开)")
    @Range(message = "状态设置在0到1之间", min = 0, max = 1, groups = VG.Update.class)
    private Integer status;
}

