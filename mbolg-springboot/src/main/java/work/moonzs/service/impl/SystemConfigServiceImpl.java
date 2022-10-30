package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.SystemConfig;
import work.moonzs.mapper.SystemConfigMapper;
import work.moonzs.service.SystemConfigService;

/**
 * 系统配置表(SystemConfig)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:27
 */
@Service("systemConfigService")
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

}

