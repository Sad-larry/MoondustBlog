package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.ExceptionLog;
import work.moonzs.mapper.ExceptionLogMapper;
import work.moonzs.service.ExceptionLogService;

/**
 * 异常日志表(ExceptionLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("exceptionLogService")
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

}

