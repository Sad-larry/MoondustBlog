package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日志表(UserLog)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 13:58:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_log")
public class UserLog {
    //主键ID    
    @TableId
    private Long id;
    //操作用户ID
    private Long userId;
    //ip地址
    private String ip;
    //操作地址
    private String address;
    //操作类型
    private String type;
    //操作日志
    private String description;
    //操作模块
    private String model;
    //操作结果
    private String result;
    //操作系统
    private String accessOs;
    //浏览器
    private String browser;
    //客户端类型
    private String clientType;
    //操作时间
    private Date createTime;
}

