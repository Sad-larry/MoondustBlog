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
public class SysConfigVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置键名
     */
    private String configKey;
    /**
     * 配置键值
     */
    private String configValue;
    /**
     * 系统内置(0否,1是)
     */
    private Integer configType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

