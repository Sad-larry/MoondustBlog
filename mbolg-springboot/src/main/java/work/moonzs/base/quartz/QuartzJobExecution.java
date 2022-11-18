package work.moonzs.base.quartz;

import org.quartz.JobExecutionContext;
import work.moonzs.domain.entity.Job;

/**
 * 允许并发执行的定时任务处理
 * 继承AbstractQuartzJob类，该父类里有任务的执行前置方法
 * 我们只需要重写doExecute执行方法，即任务的明细，就可达成任务的执行
 *
 * @author Moondust月尘
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        // 调用任务执行工具的执行方法，讲该任务解析并执行
        JobInvokeUtil.invokeMethod(job);
    }
}