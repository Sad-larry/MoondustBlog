package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户DTO")
public class WebConfigDTO {
    /**
     * 主键ID
     */
    @ApiModelProperty(notes = "主键ID")
    @NotNull(message = "主键ID不能为NULL")
    private Long id;
    /**
     * logo(文件UID)
     */
    @ApiModelProperty(notes = "logo(文件UID)")
    @NotBlank(message = "logo不能为空")
    private String logo;
    /**
     * 网站名称
     */
    @ApiModelProperty(notes = "网站名称")
    @NotBlank(message = "网站名称不能为空")
    private String name;
    /**
     * 介绍
     */
    @ApiModelProperty(notes = "介绍")
    @NotBlank(message = "网站介绍不能为空")
    private String summary;
    /**
     * 关键字
     */
    @ApiModelProperty(notes = "关键字")
    private String keyword;
    /**
     * 作者
     */
    @ApiModelProperty(notes = "作者")
    @NotBlank(message = "网站作者不能为空")
    private String author;
    /**
     * 备案号
     */
    @ApiModelProperty(notes = "备案号")
    @NotBlank(message = "网站作者不能为空")
    private String recordNum;
    /**
     * 网站地址
     */
    @ApiModelProperty(notes = "网站地址")
    @NotBlank(message = "网站地址不能为空")
    @URL(message = "网站地址无效")
    private String webUrl;
    /**
     * 支付宝收款码FileId
     */
    @ApiModelProperty(notes = "支付宝收款码FileId")
    private String aliPay;
    /**
     * 微信收款码FileId
     */
    @ApiModelProperty(notes = "微信收款码FileId")
    private String weixinPay;
    /**
     * github地址
     */
    @ApiModelProperty(notes = "github地址")
    private String github;
    /**
     * gitee地址
     */
    @ApiModelProperty(notes = "gitee地址")
    private String gitee;
    /**
     * QQ号
     */
    @ApiModelProperty(notes = "QQ号")
    private String qqNumber;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    private String email;
    /**
     * 显示的列表(用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端)
     */
    @ApiModelProperty(notes = "显示的列表(用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端)")
    private String showList;
    /**
     * 登录方式列表(用于控制前端登录方式，如账号密码,码云,Github,QQ,微信)
     */
    @ApiModelProperty(notes = "登录方式列表(用于控制前端登录方式，如账号密码,码云,Github,QQ,微信)")
    private String loginTypeList;
    /**
     * 是否开启评论(0否,1是)
     */
    @ApiModelProperty(notes = "是否开启评论(0否,1是)")
    @Range(message = "评论状态设置在0到1之间", min = 0, max = 1)
    private Integer openComment;
    /**
     * 是否开启赞赏(0否,1是)
     */
    @ApiModelProperty(notes = "是否开启赞赏(0否,1是)")
    @Range(message = "赞赏状态设置在0到1之间", min = 0, max = 1)
    private Integer openAdmiration;
    /**
     * 游客头像
     */
    @ApiModelProperty(notes = "游客头像")
    private String touristAvatar;
    /**
     * 公告
     */
    @ApiModelProperty(notes = "公告")
    @Length(message = "公告不宜太长", max = 512)
    private String bulletin;
    /**
     * 作者简介
     */
    @ApiModelProperty(notes = "作者简介")
    @Length(message = "作者简介不宜太长", max = 256)
    private String authorInfo;
    /**
     * 作者头像
     */
    @ApiModelProperty(notes = "作者头像")
    private String authorAvatar;
    /**
     * 关于我
     */
    @ApiModelProperty(notes = "关于我")
    @Length(message = "关于我介绍不宜太长", max = 256)
    private String aboutMe;
    /**
     * 是否开启音乐播放器(0否,1是)
     */
    @ApiModelProperty(notes = "是否开启音乐播放器(0否,1是)")
    @Range(message = "音乐播放器状态设置在0到1之间", min = 0, max = 1)
    private Integer isMusicPlayer;
}

