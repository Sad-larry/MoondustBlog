package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.ValidateGroup;

import javax.validation.constraints.*;
import java.util.List;

/**
 * 我觉得吧，虽然字段校验挺好的，但是如果这样写，代码太冗余了，看的不舒服，看来得好好得思索思索
 * 而且今天写的PatamCheck注解，感觉用处并不是很大阿，我写的这些DTO没有啥嵌套得对象
 * Insert: id必须为空。userId,title,content,categoryId不为空
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文章DTO")
public class ArticleDTO {
    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @Null(message = "添加文章时id必须为NULL", groups = ValidateGroup.Insert.class)
    @NotNull(message = "删除、修改、查询时id不能为NULL", groups = {ValidateGroup.Select.class, ValidateGroup.Delete.class, ValidateGroup.Update.class})
    @Min(message = "id值最小为1", value = 1L, groups = {ValidateGroup.Select.class, ValidateGroup.Delete.class, ValidateGroup.Update.class})
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(notes = "文章标题")
    @NotBlank(message = "文章'标题'不能为空", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(notes = "文章内容")
    @NotBlank(message = "文章'内容'不能为空", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
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
    @NotNull(message = "文章'分类'不能为空", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private Long categoryId;
    /**
     * 标签列表
     */
    @ApiModelProperty(notes = "标签列表")
    // @NotEmpty(message = "标签列表不能为空", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private List<Long> tagList;
    /**
     * 缩略图
     */
    @ApiModelProperty(notes = "缩略图")
    // @NotBlank(message = "缩略图不能为空", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private String thumbnail;
    /**
     * 是否置顶
     */
    @ApiModelProperty(notes = "是否置顶")
    @Pattern(regexp = "^[01]$", message = "设置置顶状态只有0、1", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private String isTop;
    /**
     * 文章状态
     */
    @ApiModelProperty(notes = "文章状态")
    @Pattern(regexp = "^[012]$", message = "设置文章状态只有0、1、2", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private String status;
    /**
     * 是否评论
     */
    @ApiModelProperty(notes = "是否允许评论")
    @Pattern(regexp = "^[01]$", message = "设置评论状态只有0、1", groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class})
    private String isComment;
}
