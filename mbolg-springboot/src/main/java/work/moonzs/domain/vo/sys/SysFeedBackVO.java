package work.moonzs.domain.vo.sys;

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
public class SysFeedBackVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 标题
     */
    private String title;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 反馈类型(1需求,2缺陷)
     */
    private Integer type;
    /**
     * 添加时间
     */
    private Date createTime;
}
