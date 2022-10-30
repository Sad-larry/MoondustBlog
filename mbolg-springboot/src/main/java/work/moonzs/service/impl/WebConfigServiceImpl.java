package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.WebConfig;
import work.moonzs.mapper.WebConfigMapper;
import work.moonzs.service.WebConfigService;

/**
 * 网站配置表(WebConfig)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:36
 */
@Service("webConfigService")
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

}

