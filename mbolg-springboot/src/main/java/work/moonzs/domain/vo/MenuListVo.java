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
public class MenuListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * pid
     */
    private Long pid;
    /**
     * url
     */
    private String path;
    /**
     * 组件
     */
    private String component;
    /**
     * 图标
     */
    private String icon;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
}
