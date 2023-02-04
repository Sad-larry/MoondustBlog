package work.moonzs.base.quartz;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import work.moonzs.base.enums.ScheduleConstants;
import work.moonzs.domain.entity.Job;
import work.moonzs.domain.entity.JobLog;
import work.moonzs.mapper.JobLogMapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author Moondust月尘
 */
@Slf4j
public abstract class AbstractQuartzJob implements org.quartz.Job {
    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        Job job = new Job();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), job);
        try {
            // 写入数据库日志功能
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            try {
                log.info("删除任务..");
                context.getScheduler().deleteJob(ScheduleUtil.getJobKey(job.getJobId(), job.getJobGroup()));
            } catch (SchedulerException ex) {
                log.error("删除任务异常  - ：", ex);
            }
            // 任务执行异常时，将任务状态设置为暂停，并且暂停该任务
            after(context, job, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     */
    protected void before(JobExecutionContext context, Job job) {
        THREAD_LOCAL.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     */
    protected void after(JobExecutionContext context, Job job, Exception e) {
        Date startTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();

        final JobLog jobLog = new JobLog();
        jobLog.setJobId(job.getJobId());
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        jobLog.setStartTime(startTime);
        jobLog.setStopTime(new Date());
        long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
        jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        // 异常不为空，说明执行有问题，状态设置为0
        if (e != null) {
            jobLog.setStatus(0);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();
            String errorMsg = StrUtil.sub(str, 0, 2000);
            jobLog.setExceptionInfo(errorMsg);
        } else {
            jobLog.setStatus(1);
        }

        // 写入数据库当中
        SpringUtil.getBean(JobLogMapper.class).insert(jobLog);
    }

    /**
     * 执行方法，由子类重写
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, Job job) throws Exception;
}
