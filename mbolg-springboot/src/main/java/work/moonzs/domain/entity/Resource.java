package work.moonzs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Resource)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_resource")
public class Resource {
    //主键
    @TableId
    private Long id;
    //资源名
    private String resourceName;
    //权限路径
    private String url;
    //请求方式
    private String requestMethod;
    //父模块id
    private Long pid;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

