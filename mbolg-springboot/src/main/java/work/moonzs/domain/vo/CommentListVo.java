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
public class CommentListVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 评论人id
     */
    private Long userId;
    /**
     * 父级评论id
     */
    private Long pid;
    /**
     * 评内容
     */
    private String content;
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
    private String url;
    /**
     * 支持(赞)
     */
    private Integer support;
    /**
     * 反对(踩)
     */
    private Integer oppose;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 评论状态(1正常,0删除)
     */
    private String status;
}