package work.moonzs.base.quartz;

import org.quartz.JobExecutionContext;
import work.moonzs.domain.entity.Job;

/**
 * 禁止并发执行的定时任务处理
 *
 * @author Moondust月尘
 */
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
