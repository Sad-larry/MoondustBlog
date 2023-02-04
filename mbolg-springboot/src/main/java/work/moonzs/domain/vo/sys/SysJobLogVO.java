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
public class SysJobLogVO {
    /**
     * 任务日志ID
     */
    private Long id;
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
     * 日志信息
     */
    private String jobMessage;
    /**
     * 执行状态(0失败,1正常)
     */
    private Integer status;
    /**
     * 异常信息
     */
    private String exceptionInfo;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date stopTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
