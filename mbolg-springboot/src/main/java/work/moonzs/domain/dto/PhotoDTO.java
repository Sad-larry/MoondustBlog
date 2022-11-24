package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
@ApiModel(description = "照片DTO")
public class PhotoDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加照片时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "照片ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 相册ID
     */
    @ApiModelProperty(notes = "相册ID")
    @NotNull(message = "相册ID不能为空", groups = {VG.Insert.class, VG.Update.class})
    private Long albumId;
    /**
     * 照片名
     */
    @ApiModelProperty(notes = "照片名")
    @NotBlank(message = "照片名不能为空", groups = {VG.Insert.class, VG.Update.class})
    @Length(message = "照片名长度在1到40个字符", min = 1, max = 40, groups = {VG.Insert.class, VG.Update.class})
    private String name;
    /**
     * 照片描述
     */
    @ApiModelProperty(notes = "照片描述")
    @Length(message = "照片描述不宜太长", max = 256, groups = {VG.Insert.class, VG.Update.class})
    private String info;
    /**
     * 照片地址
     */
    @ApiModelProperty(notes = "照片地址")
    @NotNull(message = "照片地址不能为空", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "照片地址无效", groups = {VG.Insert.class, VG.Update.class})
    private String url;
}

