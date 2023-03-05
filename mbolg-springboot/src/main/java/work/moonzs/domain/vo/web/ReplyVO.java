package work.moonzs.domain.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyVO {
    /**
     * 评论id
     */
    private Long id;
    /**
     * 父评论id
     */
    private Long parentId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 被回复用户id
     */
    private Long replyUserId;
    /**
     * 被回复用户头像
     */
    private String replyAvatar;
    /**
     * 被回复用户昵称
     */
    private String replyNickname;
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
}
