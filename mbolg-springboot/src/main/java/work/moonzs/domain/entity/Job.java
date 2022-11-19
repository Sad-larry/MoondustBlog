package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 定时任务调度表(Job)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_job")
public class Job {
    //任务ID    
    @TableId
    private Long jobId;
    //任务名称
    private String jobName;
    //任务组名
    private String jobGroup;
    //调用目标字符串
    private String invokeTarget;
    //cron执行表达式
    private String cronExpression;
    //计划执行错误策略(0默认策略,1立即执行,2执行一次,3放弃执行)
    private Integer misfirePolicy;
    //是否并发执行(0禁止,1允许)
    private Integer concurrent;
    //任务状态(0暂停,1正常)
    private Integer status;
    //备注信息
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

