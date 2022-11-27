package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.WebConfig;
import work.moonzs.domain.vo.sys.SysWebConfigVO;

/**
 * 网站配置表(WebConfig)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:37:29
 */
public interface WebConfigService extends IService<WebConfig> {

    /**
     * 获取网络配置
     *
     * @return {@link SysWebConfigVO}
     */
    SysWebConfigVO getWebConfig();

    /**
     * 更新网站配置
     *
     * @param webConfig 网络配置
     * @return boolean
     */
    @Transactional
    boolean updateWebConfig(WebConfig webConfig);

    /**
     * 获取基本的网络配置信息
     * 一般是给博客前端的数据
     *
     * @return {@link SysWebConfigVO}
     */
    SysWebConfigVO getBaseWebConfig();
}

