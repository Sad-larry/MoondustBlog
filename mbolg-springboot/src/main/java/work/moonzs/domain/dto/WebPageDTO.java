package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@ApiModel(description = "页面DTO")
public class WebPageDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "添加页面时ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "页面ID不能为空", groups = VG.Update.class)
    private Long id;
    /**
     * 页面名称
     */
    @ApiModelProperty(notes = "页面名称")
    @NotBlank(message = "页面名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String pageName;
    /**
     * 页面标签
     */
    @ApiModelProperty(notes = "页面标签")
    @NotBlank(message = "页面标签不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String pageLabel;
    /**
     * 页面图源
     */
    @ApiModelProperty(notes = "页面图源")
    @NotBlank(message = "页面图源不能为空", groups = {VG.Insert.class, VG.Update.class})
    @URL(message = "页面图源不符合链接", groups = {VG.Insert.class, VG.Update.class})
    private String pageCover;
}

