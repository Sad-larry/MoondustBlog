package work.moonzs.domain.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 评论人id
     */
    private Long userId;
    /**
     * 评论人头像
     */
    private String avatar;
    /**
     * 评论人昵称
     */
    private String nickname;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 评论时间
     */
    private Date createTime;
    /**
     * 回复量
     */
    private Integer replyCount;
    /**
     * 回复列表
     */
    private List<ReplyVO> replyVOList;
}
