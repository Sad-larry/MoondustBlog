package work.moonzs.base.quartz;

import org.quartz.*;
import work.moonzs.base.enums.ScheduleConstants;
import work.moonzs.base.exception.ServiceException;
import work.moonzs.domain.entity.Job;

/**
 * @author blue
 * @date 2021/12/8
 * @apiNote
 */
public class ScheduleUtil {
    /**
     * 得到quartz任务类
     *
     * @param job 任务
     * @return 具体执行任务类
     */
    private static Class<? extends org.quartz.Job> getQuartzJobClass(Job job) {
        // 是否并发执行(0禁止,1允许)
        return ScheduleConstants.Status.NORMAL.getValue().equals(job.getConcurrent())
                ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     *
     * @param scheduler 调度器
     * @param job       任务
     * @throws SchedulerException 调度程序异常
     */
    public static void createScheduleJob(Scheduler scheduler, Job job) throws SchedulerException {
        Class<? extends org.quartz.Job> jobClass = getQuartzJobClass(job);
        // 构建job信息
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder
                .newJob(jobClass)
                .withIdentity(getJobKey(jobId, jobGroup))
                .build();
        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder)
                .build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(Job job, CronScheduleBuilder cb)
            throws ServiceException {
        // 因为Integer包装数据类型不能用switch判断，switch用==判断的
        Integer misfirePolicy = job.getMisfirePolicy();
        if (misfirePolicy.equals(ScheduleConstants.MISFIRE_DEFAULT)) {
            return cb;
        } else if (misfirePolicy.equals(ScheduleConstants.MISFIRE_IGNORE_MISFIRES)) {
            // 不触发立即执行
            // ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
            return cb.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (misfirePolicy.equals(ScheduleConstants.MISFIRE_FIRE_AND_PROCEED)) {
            // 以错过的第一个频率时间立刻开始执行
            // ——重做错过的所有频率周期后
            // ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
            return cb.withMisfireHandlingInstructionFireAndProceed();
        } else if (misfirePolicy.equals(ScheduleConstants.MISFIRE_DO_NOTHING)) {
            // 不触发立即执行
            // ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
            return cb.withMisfireHandlingInstructionDoNothing();
        } else {
            throw new ServiceException("The task misfire policy '" + job.getMisfirePolicy() + "' cannot be used in cron schedule tasks");
        }
    }
}
