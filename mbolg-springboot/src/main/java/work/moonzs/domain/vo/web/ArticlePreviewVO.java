package work.moonzs.domain.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreviewVO {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章内容md版
     */
    private String contentMd;
    /**
     * 文章封面地址
     */
    private String avatar;
    /**
     * 是否置顶(0否,1是)
     */
    private Integer isStick;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 分类名
     */
    private String categoryName;
    /**
     * 标签列表
     */
    private List<TagVO> tagVOList;
}
