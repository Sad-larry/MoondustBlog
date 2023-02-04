package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.JobLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysJobLogVO;

/**
 * 定时任务调度日志表(JobLog)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface JobLogService extends IService<JobLog> {

    /**
     * 定时任务日志列表
     *
     * @return {@link PageVO}<{@link SysJobLogVO}>
     */
    PageVO<SysJobLogVO> listJobLog(Integer pageNum, Integer pageSize, JobLog jobLog);

    /**
     * 删除定时任务日志
     *
     * @param ids id
     * @return boolean
     */
    @Transactional
    boolean deleteJobLog(Long[] ids);

    /**
     * 清除定时任务日志
     *
     * @return boolean
     */
    @Transactional
    boolean cleanJobLog();
}

