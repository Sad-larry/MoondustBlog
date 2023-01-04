package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作日志表(AdminLog)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_admin_log")
public class AdminLog {
    //主键ID    
    @TableId
    private Long id;
    //操作用户
    private String username;
    //请求接口
    private String requestUrl;
    //请求方式
    private String type;
    //操作名称
    private String operationName;
    //ip
    private String ip;
    //ip来源
    private String source;
    //请求接口耗时
    private Long spendTime;
    //请求参数
    private String paramsJson;
    //类地址
    private String classPath;
    //方法名
    private String methodName;
    //创建时间
    private Date createTime;
}

