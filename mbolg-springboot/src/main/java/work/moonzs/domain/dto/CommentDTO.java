package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "评论DTO")
public class CommentDTO {
    /**
     * 父评论id
     */
    @ApiModelProperty(name = "parentId", value = "评论父id", dataType = "Long")
    private Long parentId;
    /**
     * 评论文章id
     */
    @ApiModelProperty(name = "articleId", value = "文章id", required = true, dataType = "Long")
    @NotNull(message = "{CommentDTO.articleId.NotNull}")
    private Long articleId;
    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户ID", required = true, dataType = "Long")
    @NotNull(message = "{CommentDTO.userId.NotNull}")
    private Long userId;
    /**
     * 回复用户id
     */
    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Long")
    private Long replyUserId;
    /**
     * 评论内容
     */
    @ApiModelProperty(name = "content", value = "评论内容", required = true, dataType = "String")
    @NotBlank(message = "{CommentDTO.content.NotBlank}")
    private String content;
}
