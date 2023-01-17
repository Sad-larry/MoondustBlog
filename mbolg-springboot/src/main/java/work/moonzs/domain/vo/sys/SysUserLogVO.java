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
public class SysUserLogVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 操作用户ID
     */
    private Long userId;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 操作地址
     */
    private String address;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作日志
     */
    private String description;
    /**
     * 操作模块
     */
    private String model;
    /**
     * 操作结果
     */
    private String result;
    /**
     * 操作系统
     */
    private String accessOs;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 客户端类型
     */
    private String clientType;
    /**
     * 操作时间
     */
    private Date createTime;
}
