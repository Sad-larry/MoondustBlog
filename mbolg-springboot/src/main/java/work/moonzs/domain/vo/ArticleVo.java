package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 文章内容md版
     */
    private String contentMd;
    /**
     * 文章封面地址
     */
    private String avatar;
    /**
     * 文章简介
     */
    private String summary;
    /**
     * 文章阅读量
     */
    private Integer quantity;
    /**
     * 是否是私密文章(0否,1是)
     */
    private Integer isSecret;
    /**
     * 是否置顶(0否,1是)
     */
    private Integer isStick;
    /**
     * 是否发布(0草稿,1发布)
     */
    private Integer isPublish;
    /**
     * 是否原创(0转载,1原创)
     */
    private Integer isOriginal;
    /**
     * 转载地址
     */
    private String originalUrl;
    /**
     * 说明
     */
    private String remark;
    /**
     * SEO关键词
     */
    private String keywords;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 分类名
     */
    private String categoryName;
    /**
     * 标签名集合
     */
    private String tagNames;
}
