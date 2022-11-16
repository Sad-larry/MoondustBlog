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
public class SysJobVO {
    /**
     * 任务ID
     */
    private Long jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务组名
     */
    private String jobGroup;
    /**
     * 调用目标字符串
     */
    private String invokeTarget;
    /**
     * cron执行表达式
     */
    private String cronExpression;
    /**
     * 计划执行错误策略(1立即执行,2执行一次,3放弃执行)
     */
    private Integer misfirePolicy;
    /**
     * 是否并发执行(0禁止,1允许)
     */
    private Integer concurrent;
    /**
     * 任务状态(0暂停,1正常)
     */
    private Integer status;
    /**
     * 备注信息
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
    /**
     * 下一次执行时间
     */
    private Date nextValidTime;
}

