package work.moonzs.base.quartz;

import org.quartz.CronExpression;

import java.util.Date;

/**
 * @author Moondust月尘
 */
public class CronUtil {
    /**
     * 验证Cron表达式是否有效
     *
     * @param cronExpression cron表达式
     * @return boolean
     */
    public static boolean idValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 返回下一个执行时间根据给定的Cron表达式
     *
     * @param cronExpression Cron表达式
     * @return Date 下次Cron表达式执行时间
     */
    public static Date getNextExecution(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
