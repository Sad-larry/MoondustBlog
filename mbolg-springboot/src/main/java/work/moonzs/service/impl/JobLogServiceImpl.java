package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.JobLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysJobLogVO;
import work.moonzs.mapper.JobLogMapper;
import work.moonzs.service.JobLogService;

import java.util.List;

/**
 * 定时任务调度日志表(JobLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("jobLogService")
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    @Override
    public PageVO<SysJobLogVO> listJobLog(Integer pageNum, Integer pageSize, JobLog jobLog) {
        LambdaQueryWrapper<JobLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(JobLog::getCreateTime);
        queryWrapper.eq(ObjUtil.isNotNull(jobLog.getJobId()), JobLog::getJobId, jobLog.getJobId());
        queryWrapper.like(StrUtil.isNotBlank(jobLog.getJobName()), JobLog::getJobName, jobLog.getJobName());
        queryWrapper.like(StrUtil.isNotBlank(jobLog.getJobGroup()), JobLog::getJobGroup, jobLog.getJobGroup());
        queryWrapper.eq(ObjUtil.isNotNull(jobLog.getStatus()), JobLog::getStatus, jobLog.getStatus());
        queryWrapper.between(
                ObjUtil.isNotNull(jobLog.getStartTime()) && ObjUtil.isNotNull(jobLog.getStopTime()),
                JobLog::getStartTime, jobLog.getStartTime(), jobLog.getStopTime());
        Page<JobLog> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<SysJobLogVO> sysJobLogVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysJobLogVO.class);
        return new PageVO<>(sysJobLogVOS, page.getTotal());
    }

    @Override
    public boolean deleteJobLog(Long[] jobLogIds) {
        return removeBatchByIds(List.of(jobLogIds));
    }

    @Override
    public boolean cleanJobLog() {
        return remove(new QueryWrapper<>());
    }
}

