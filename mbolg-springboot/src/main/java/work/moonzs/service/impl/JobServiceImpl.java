package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Job;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysJobVO;
import work.moonzs.mapper.JobMapper;
import work.moonzs.service.JobService;

import java.util.Arrays;
import java.util.Date;

/**
 * 定时任务调度表(Job)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("jobService")
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Override
    public boolean isExistJobById(Long jobId) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getJobId, jobId);
        return count(queryWrapper) > 0;
    }

    @Override
    public PageVO<SysJobVO> listJob(String jobName, String jobGroup, Integer status) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(jobName), Job::getJobName, jobName)
                .eq(StrUtil.isNotBlank(jobGroup), Job::getJobGroup, jobGroup)
                .eq(Job::getStatus, status);
        Page<Job> page = new Page<>(1, 10);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysJobVO.class), page.getTotal());
    }

    @Override
    public SysJobVO getJobById(Long jobId) {
        Job byId = getById(jobId);
        BusinessAssert.notNull(byId, AppHttpCodeEnum.DATA_NOT_EXIST);
        SysJobVO sysJobVO = BeanCopyUtil.copyBean(byId, SysJobVO.class);
        // TODO 由cron表达式查询下一次执行时间
        sysJobVO.setNextValidTime(new Date());
        return sysJobVO;
    }

    @Override
    public Long insertJob(Job job) {
        getBaseMapper().insert(job);
        // TODO 添加到定时任务中
        return job.getJobId();
    }

    @Override
    public boolean updateJob(Job job) {
        BusinessAssert.isTure(isExistJobById(job.getJobId()), AppHttpCodeEnum.DATA_NOT_EXIST);
        updateById(job);
        // TODO 修改定时任务中的作业
        return true;
    }

    @Override
    public boolean deleteJob(Long[] jobIds) {
        Arrays.stream(jobIds).forEach(jobId -> {
            Job byId = getById(jobId);
            if (ObjUtil.isNotNull(byId)) {
                if (removeById(jobId)) {
                    // TODO 删除定时任务中的作业
                }
            }
        });
        return true;
    }
}

