package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 评论表(Comment)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    //主键ID    
    @TableId
    private Long id;
    //文章ID
    private Long articleId;
    //评论人ID
    private Long userId;
    //评论内容
    private String content;
    //评论时间
    private Date createTime;
    //父评论ID
    private Long parentId;
    //回复人ID
    private Long replyUserId;
}

