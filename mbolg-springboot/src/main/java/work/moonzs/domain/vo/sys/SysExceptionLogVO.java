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
public class SysExceptionLogVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * IP
     */
    private String ip;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 描述
     */
    private String operation;
    /**
     * 参数
     */
    private String params;
    /**
     * 异常对象json格式
     */
    private String exceptionJson;
    /**
     * 异常简单信息,等同于e.getMessage
     */
    private String exceptionMessage;
    /**
     * 发生时间
     */
    private Date createTime;
}
