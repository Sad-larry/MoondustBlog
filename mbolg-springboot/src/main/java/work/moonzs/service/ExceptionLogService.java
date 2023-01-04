package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.ExceptionLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysExceptionLogVO;

/**
 * 异常日志表(ExceptionLog)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface ExceptionLogService extends IService<ExceptionLog> {

    /**
     * 异常日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVO}<{@link SysExceptionLogVO}>
     */
    PageVO<SysExceptionLogVO> listExceptionLog(Integer pageNum, Integer pageSize);

    /**
     * 批量删除异常日志
     *
     * @param exceptionLogIds 异常日志id
     * @return boolean
     */
    @Transactional
    boolean deleteExceptionLog(Long[] exceptionLogIds);
}

