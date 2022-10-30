package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.AdminLog;
import work.moonzs.mapper.AdminLogMapper;
import work.moonzs.service.AdminLogService;

/**
 * 操作日志表(AdminLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:38:42
 */
@Service("adminLogService")
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

}

