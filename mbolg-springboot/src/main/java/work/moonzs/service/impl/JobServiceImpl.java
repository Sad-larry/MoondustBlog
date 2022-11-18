package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.ScheduleConstants;
import work.moonzs.base.quartz.CronUtil;
import work.moonzs.base.quartz.ScheduleUtil;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Job;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysJobVO;
import work.moonzs.mapper.JobMapper;
import work.moonzs.service.JobService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 定时任务调度表(Job)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("jobService")
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    private final Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     */
    @PostConstruct
    public void init() throws SchedulerException {
        // 清除调度器所有数据
        scheduler.clear();
        // 从数据库中查询所有的任务
        List<Job> jobs = list();
        // 将任务添加到调度器中
        for (Job job : jobs) {
            ScheduleUtil.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public boolean isExistJobById(Long jobId) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getJobId, jobId);
        return count(queryWrapper) > 0;
    }

    @Override
    public PageVO<SysJobVO> listJob(String jobName, String jobGroup, Integer status) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(jobName), Job::getJobName, jobName).eq(StrUtil.isNotBlank(jobGroup), Job::getJobGroup, jobGroup).eq(Job::getStatus, status);
        Page<Job> page = new Page<>(1, 10);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysJobVO.class), page.getTotal());
    }

    @Override
    public SysJobVO getJobById(Long jobId) {
        Job byId = getById(jobId);
        BusinessAssert.notNull(byId, AppHttpCodeEnum.DATA_NOT_EXIST);
        SysJobVO sysJobVO = BeanCopyUtil.copyBean(byId, SysJobVO.class);
        // 由cron表达式查询下一次执行时间
        sysJobVO.setNextValidTime(CronUtil.getNextExecution(byId.getCronExpression()));
        return sysJobVO;
    }

    @Override
    public Long insertJob(Job job) throws SchedulerException {
        getBaseMapper().insert(job);
        // 添加到定时任务中
        ScheduleUtil.createScheduleJob(scheduler, job);
        return job.getJobId();
    }

    @Override
    public boolean updateJob(Job job) throws SchedulerException {
        BusinessAssert.isTure(isExistJobById(job.getJobId()), AppHttpCodeEnum.DATA_NOT_EXIST);
        boolean update = updateById(job);
        if (update) {
            // 修改定时任务中的作业
            updateSchedulerJob(job, job.getJobGroup());
        }
        return update;
    }

    @Override
    public boolean deleteJob(Long[] jobIds) throws SchedulerException {
        List<JobKey> jobKeys = new ArrayList<>();
        Arrays.stream(jobIds).forEach(jobId -> {
            Job byId = getById(jobId);
            if (ObjUtil.isNotNull(byId)) {
                if (removeById(jobId)) {
                    jobKeys.add(ScheduleUtil.getJobKey(jobId, byId.getJobGroup()));
                }
            }
        });
        // 删除定时任务中的作业
        scheduler.deleteJobs(jobKeys);
        return true;
    }

    @Override
    public void runJob(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 通过jobId以及jobGroup查找任务
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getJobId, jobId).eq(Job::getJobGroup, jobGroup);
        Job one = getOne(queryWrapper);
        // 数据得存在
        BusinessAssert.notNull(one, AppHttpCodeEnum.DATA_NOT_EXIST);
        // 任务调度里面立即执行该任务
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, one);
        // 立即执行一次任务
        scheduler.triggerJob(ScheduleUtil.getJobKey(jobId, jobGroup), dataMap);

    }

    @Override
    public void pauseJob(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        BusinessAssert.isTure(isExistJobById(jobId), AppHttpCodeEnum.DATA_NOT_EXIST);
        LambdaUpdateWrapper<Job> updateWrapper = new LambdaUpdateWrapper<>();
        // 加ne是为了当修改运行的作业时，修改状态为运行产生的无效运行操作
        // 查询不到该数据当然就修改失败了
        updateWrapper.eq(Job::getJobId, jobId).ne(Job::getStatus, ScheduleConstants.Status.PAUSE.getValue()).set(Job::getStatus, ScheduleConstants.Status.PAUSE.getValue());
        boolean update = update(updateWrapper);
        if (update) {
            // 任务调度暂停该任务
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
    }

    @Override
    public boolean changeJobStatus(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        Integer status = job.getStatus();
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getJobId, jobId).eq(Job::getJobGroup, jobGroup);
        Job one = getOne(queryWrapper);
        BusinessAssert.notNull(one, AppHttpCodeEnum.DATA_NOT_EXIST);

        // 如果任务本身就是运行状态则不用更改
        if (one.getStatus().equals(status)) {
            if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
                BusinessAssert.fail(AppHttpCodeEnum.JOB_ON_RUNNING);
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
                BusinessAssert.fail(AppHttpCodeEnum.JOB_ON_PAUSING);
            }
        }
        // 更新状态
        LambdaUpdateWrapper<Job> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Job::getJobId, jobId).set(Job::getStatus, status);
        boolean update = update(updateWrapper);
        if (update) {
            if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
                scheduler.resumeJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
                scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
            }
        }
        return update;
    }

    /**
     * 更新调度任务
     *
     * @param job      任务
     * @param jobGroup 任务组
     */
    private void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException {
        Long jobId = job.getJobId();
        JobKey jobKey = ScheduleUtil.getJobKey(jobId, jobGroup);
        // 判断任务是否存在
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtil.createScheduleJob(scheduler, job);
    }
}

