package work.moonzs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Moondust月尘
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {
    /**
     * 线程池维护线程的最少数量
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 线程池维护线程的最大数量
     */
    private static final int MAX_POOL_SIZE = 100;
    /**
     * 线程池维护线程的前缀名称
     */
    private static final String THREAD_NAME_PREFIX = "MOON-THREAD-TASK-";
    /**
     * 线程池所使用的缓冲队列
     */
    private static final int QUEUE_CAPACITY = 20;
    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static final int KEEP_ALIVE_SECONDS = 30000;
    /**
     * 设置等待任务完成关闭，不中断正在运行的任务并执行队列中的所有任务
     */
    private static final boolean WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN = true;


    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor serviceJobTaskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        poolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        poolTaskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        poolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        poolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN);
        poolTaskExecutor.setRejectedExecutionHandler(executionHandler(1));
        // 线程初始化
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    /**
     * 拒绝处理策略
     * <ul>
     *     <li>1、ThreadPoolExecutor.AbortPolicy策略，是默认的策略,处理程序遭到拒绝将抛出运行时 RejectedExecutionException</li>
     *     <li>2、ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃</li>
     *     <li>3、ThreadPoolExecutor.DiscardPolicy策略，不能执行的任务将被丢弃</li>
     *     <li>4、ThreadPoolExecutor.DiscardOldestPolicy策略，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）</li>
     *     <li>5、自定义策略：当然也可以根据应用场景需要来实现RejectedExecutionHandler接口自定义策略。如记录日志或持久化不能处理的任务</li>
     * </ul>
     *
     * @return {@link RejectedExecutionHandler}
     */
    private RejectedExecutionHandler executionHandler(final int type) {
        return switch (type) {
            case 1 -> new ThreadPoolExecutor.AbortPolicy();
            case 2 -> new ThreadPoolExecutor.CallerRunsPolicy();
            case 3 -> new ThreadPoolExecutor.DiscardPolicy();
            case 4 -> new ThreadPoolExecutor.DiscardOldestPolicy();
            case 5 -> new MyRejectedExecutionHandler();
            default -> throw new IllegalArgumentException("策略模式不合法");
        };
    }

    /**
     * 自定义的拒绝处理策略
     * 一般用作记录日志或持久化不能处理的任务
     *
     * @author Moondust月尘
     * @date 2022/12/06
     */
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // TODO 自定义拒绝处理策略
        }
    }
}
