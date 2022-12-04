package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统配置表(SystemConfig)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_system_config")
public class SystemConfig {
    //主键ID
    @TableId
    private Long id;
    //配置名称
    private String configName;
    //配置键名
    private String configKey;
    //配置键值
    private String configValue;
    //系统内置(0否,1是)
    private Integer configType;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

