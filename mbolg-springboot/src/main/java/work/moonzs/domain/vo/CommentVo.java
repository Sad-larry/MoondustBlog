package work.moonzs.domain.vo;

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
public class CommentVo {
    /**
     * 主键
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
     * 评论人网站地址
     */
    private String webSite;
    /**
     * 评论内容
     */
    private String commentContent;
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
    private List<ReplyVo> replyVoList;
}