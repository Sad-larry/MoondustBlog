package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 异常日志表(ExceptionLog)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_exception_log")
public class ExceptionLog {
    //主键ID    
    @TableId
    private Long id;
    //用户名
    private String username;
    //IP
    private String ip;
    //ip来源
    private String ipSource;
    //请求方法
    private String method;
    //描述
    private String operation;
    //参数
    private String params;
    //异常对象json格式
    private String exceptionJson;
    //异常简单信息,等同于e.getMessage
    private String exceptionMessage;
    //发生时间
    private Date createTime;
}

