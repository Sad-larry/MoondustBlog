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
public class SysWebPageVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 页面名称
     */
    private String pageName;
    /**
     * 页面标签
     */
    private String pageLabel;
    /**
     * 页面图源
     */
    private String pageCover;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

