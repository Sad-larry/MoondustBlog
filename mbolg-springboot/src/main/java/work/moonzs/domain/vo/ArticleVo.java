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
public class ArticleVo {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 总结
     */
    private String summary;
    /**
     * 分类
     */
    private CategoryVo categoryVo;
    /**
     * 标记列表
     */
    private List<TagVo> tagListVo;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 是否置顶
     */
    private String isTop;
    /**
     * 状态
     */
    private String status;
    /**
     * 浏览量
     */
    private Long viewCount;
    /**
     * 是否允许评论
     */
    private String isComment;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 星数
     */
    private Long starCount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Data updateTime;
}
