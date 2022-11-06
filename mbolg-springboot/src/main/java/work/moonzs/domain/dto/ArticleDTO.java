package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.validate.VG;

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
    @Null(message = "添加文章时id必须为NULL", groups = VG.Insert.class)
    @NotNull(message = "修改时id不能为NULL", groups = VG.Update.class)
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty(notes = "userId")
    private Long userId;
    /**
     * 标题
     */
    @ApiModelProperty(notes = "文章标题")
    @NotBlank(message = "文章'标题'不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String title;
    /**
     * 文章封面地址
     */
    @ApiModelProperty(notes = "文章封面")
    private String avatar;
    /**
     * 文章简介
     */
    @ApiModelProperty(notes = "文章简介")
    @NotBlank(message = "文章'简介'不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String summary;
    /**
     * 内容
     */
    @ApiModelProperty(notes = "文章内容")
    private String content;
    /**
     * 内容md版
     */
    @ApiModelProperty(notes = "文章内容md版")
    private String contentMd;
    /**
     * 是否是私密文章
     */
    @ApiModelProperty(notes = "是否是私密文章")
    @Min(value = 0, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer isSecret;
    /**
     * 是否置顶
     */
    @ApiModelProperty(notes = "是否置顶")
    @Min(value = 0, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer isStick;
    /**
     * 是否发布
     */
    @ApiModelProperty(notes = "是否发布")
    @Min(value = 0, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer isPublish;
    /**
     * 是否原创
     */
    @ApiModelProperty(notes = "是否原创")
    @Min(value = 0, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer isOriginal;
    /**
     * 原创url
     */
    @ApiModelProperty(notes = "原创url")
    private String originalUrl;
    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    private String remark;
    /**
     * SEO关键字
     */
    @ApiModelProperty(notes = "SEO关键字")
    private String keywords;

    /// --- 非表中数据，但是根据此字段去数据库中查找响应id并存入关联表中 --- ///
    /**
     * 分类名称
     */
    @ApiModelProperty(notes = "分类名称")
    @NotBlank(message = "分类名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String categoryName;
    /**
     * 标签列表
     */
    @ApiModelProperty(notes = "标签列表")
    @NotEmpty(message = "标签列表不能为空", groups = {VG.Insert.class, VG.Update.class})
    private List<String> tags;
}
