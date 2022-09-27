package work.moonzs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Article)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article")
public class Article {
    //主键    
    @TableId
    private Long id;
    //作者
    private Long userId;
    //文章标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //是否置顶(1是,0否)
    private String isTop;
    //文章状态(1发布,0草稿,-1待删除)
    private String status;
    //浏览量
    private Long viewCount;
    //是否允许评论(1是,0否)
    private String isComment;
    //评论数
    private Long commentCount;
    //点赞数
    private Long starCount;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    private Date updateTime;
}

