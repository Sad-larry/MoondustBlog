package work.moonzs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Comment)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    //主键
    @TableId
    private Long id;
    //文章id
    private Long articleId;
    //评论人id
    private Long userId;
    //父级评论id
    private Long pid;
    //评论内容
    private String content;
    //评论人头像
    private String avatar;
    //评论人昵称
    private String nickname;
    //评论人网站地址
    private String url;
    //支持(赞)
    private Integer support;
    //反对(踩)
    private Integer oppose;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

