package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.UserLog;
import work.moonzs.mapper.UserLogMapper;
import work.moonzs.service.UserLogService;

/**
 * 日志表(UserLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 13:58:37
 */
@Service("userLogService")
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

}

