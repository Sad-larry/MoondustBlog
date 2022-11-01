package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 博客文章表(Article)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article")
public class Article {
    //主键ID    
    @TableId
    private Long id;
    //用户ID
    private Long userId;
    //文章标题
    private String title;
    //文章内容
    private String content;
    //文章简介
    private String summary;
    //分类ID
    private Long categoryId;
    //文章封面地址
    private String avatar;
    //文章内容md版
    private String contentMd;
    //是否是私密文章(0否,1是)
    private Integer isSecret;
    //是否置顶(0否,1是)
    private Integer isStick;
    //是否发布(0草稿,1发布)
    private Integer isPublish;
    //是否原创(0转载,1原创)
    private Integer isOriginal;
    //转载地址
    private String originalUrl;
    //文章阅读量
    private Integer quantity;
    //说明
    private String remark;
    //SEO关键词
    private String keywords;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

