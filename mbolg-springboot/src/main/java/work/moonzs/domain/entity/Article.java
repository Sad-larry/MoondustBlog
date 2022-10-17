package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Article)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:05
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
    //是否置顶(0否,1是)
    private Integer isTop;
    //文章状态(0草稿,1发布,2待删除)
    private Integer status;
    //浏览量
    private Long viewCount;
    //是否允许评论(0否,1是)
    private Integer isComment;
    //评论数
    private Long commentCount;
    //点赞数
    private Long starCount;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

