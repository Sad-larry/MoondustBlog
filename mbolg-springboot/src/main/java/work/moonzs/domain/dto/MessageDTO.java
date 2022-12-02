package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "留言DTO")
public class MessageDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @Null(message = "{MessageDTO.id.Null}")
    private Long id;
    /**
     * 内容
     */
    @ApiModelProperty(notes = "内容")
    @NotBlank(message = "{MessageDTO.content.NotBlank}")
    private String content;
    /**
     * 用户昵称
     */
    @ApiModelProperty(notes = "用户昵称")
    @NotBlank(message = "{MessageDTO.nickname.NotBlank}")
    private String nickname;
    /**
     * 用户头像
     */
    @ApiModelProperty(notes = "用户头像")
    private String avatar;
    /**
     * ip地址
     */
    @ApiModelProperty(notes = "ip地址")
    private String ipAddress;
    /**
     * ip来源
     */
    @ApiModelProperty(notes = "ip来源")
    private String ipSource;
    /**
     * 留言时间
     */
    @ApiModelProperty(notes = "留言时间")
    @Null(message = "MessageDTO.time.NotBlank")
    private Long time;
}

