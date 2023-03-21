package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.*;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "反馈DTO")
public class FeedBackDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "{FeedBackDTO.id.Null}", groups = {VG.Insert.class})
    @NotNull(message = "{FeedBackDTO.id.NotNull}", groups = {VG.Update.class})
    private Long id;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    @NotBlank(message = "{FeedBackDTO.email.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    @Email(message = "{FeedBackDTO.email.Email}", groups = {VG.Insert.class, VG.Update.class})
    private String email;
    /**
     * 标题
     */
    @ApiModelProperty(notes = "标题")
    @NotBlank(message = "{FeedBackDTO.title.NotBlank}", groups = {VG.Insert.class, VG.Update.class})
    private String title;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 回复内容
     */
    @ApiModelProperty(notes = "回复内容")
    @Null(message = "{FeedBackDTO.replyContent.Null}", groups = {VG.Insert.class})
    @NotBlank(message = "{FeedBackDTO.replyContent.NotBlank}", groups = {VG.Update.class})
    private String replyContent;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 反馈类型(1需求,2缺陷)
     */
    @Min(value = 1, message = "{FeedBackDTO.type.Min}", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 2, message = "{FeedBackDTO.type.Max}", groups = {VG.Insert.class, VG.Update.class})
    private Integer type;
}

