package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "{FeedBackDTO.id.Null}")
    private Long id;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    @NotBlank(message = "{FeedBackDTO.email.NotBlank}")
    @Email(message = "{FeedBackDTO.email.Email}")
    private String email;
    /**
     * 标题
     */
    @ApiModelProperty(notes = "标题")
    @NotBlank(message = "{FeedBackDTO.title.NotBlank}")
    private String title;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 反馈类型(1需求,2缺陷)
     */
    @Min(value = 1, message = "{FeedBackDTO.type.Min}")
    @Max(value = 2, message = "{FeedBackDTO.type.Max}")
    private Integer type;
}

