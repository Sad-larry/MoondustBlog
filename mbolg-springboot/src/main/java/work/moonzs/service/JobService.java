package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Job;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysJobVO;

/**
 * 定时任务调度表(Job)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface JobService extends IService<Job> {

    /**
     * 通过任务id判断数据是否存在
     *
     * @param jobId 任务id
     * @return boolean
     */
    boolean isExistJobById(Long jobId);

    /**
     * 获取定时任务列表
     *
     * @param jobName  作业名
     * @param jobGroup 工作小组
     * @param status   状态
     * @return {@link PageVO}<{@link SysJobVO}>
     */
    PageVO<SysJobVO> listJob(String jobName, String jobGroup, Integer status);

    /**
     * 通过id查询任务
     *
     * @param jobId 任务id
     * @return {@link SysJobVO}
     */
    SysJobVO getJobById(Long jobId);

    /**
     * 添加任务
     *
     * @param job 任务
     * @return boolean
     */
    @Transactional
    Long insertJob(Job job) throws SchedulerException;

    /**
     * 更新任务
     *
     * @param job 任务
     * @return boolean
     */
    @Transactional
    boolean updateJob(Job job) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param jobIds 任务ids
     * @return boolean
     */
    @Transactional
    boolean deleteJob(Long[] jobIds) throws SchedulerException;

    /**
     * 立即执行任务
     *
     * @param job 任务
     */
    @Transactional
    void runJob(Job job) throws SchedulerException;

    /**
     * 立即暂停任务
     *
     * @param job 任务
     */
    @Transactional
    void pauseJob(Job job) throws SchedulerException;

    /**
     * 修改任务状态
     *
     * @param job 任务
     * @return boolean
     */
    @Transactional
    boolean changeJobStatus(Job job) throws SchedulerException;
}

