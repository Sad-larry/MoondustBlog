package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文章DTO")
public class ArticleDTO {
    /**
     * 标题
     */
    @ApiModelProperty(notes = "文章标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(notes = "文章内容")
    private String content;
    /**
     * 总结
     */
    @ApiModelProperty(notes = "文章摘要")
    private String summary;
    /**
     * 分类id
     */
    @ApiModelProperty(notes = "分类id")
    private Long categoryId;
    /**
     * 标签列表
     */
    @ApiModelProperty(notes = "标签列表")
    private List<Long> tagList;
    /**
     * 缩略图
     */
    @ApiModelProperty(notes = "缩略图")
    private String thumbnail;
    /**
     * 是否置顶
     */
    @ApiModelProperty(notes = "是否置顶")
    private String isTop;
    /**
     * 文章状态
     */
    @ApiModelProperty(notes = "文章状态")
    private String status;
    /**
     * 是评论
     */
    @ApiModelProperty(notes = "是否允许评论")
    private String isComment;
}
