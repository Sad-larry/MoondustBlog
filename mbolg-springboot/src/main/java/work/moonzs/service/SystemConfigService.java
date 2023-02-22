package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.SystemConfig;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysConfigVO;

/**
 * 系统配置表(SystemConfig)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:53
 */
public interface SystemConfigService extends IService<SystemConfig> {
    /**
     * 加载配置缓存数据
     */
    void loadingConfigCache();

    /**
     * 查询系统配置信息
     *
     * @param configId 配置ID
     * @return {@link SysConfigVO}
     */
    SysConfigVO selectConfigById(Long configId);

    /**
     * 根据键名查询系统配置信息
     *
     * @param configKey 配置键名
     * @return {@link String}
     */
    String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return boolean
     */
    boolean selectCaptchaEnabled();

    /**
     * 获取默认注册头像
     *
     * @return {@link String}
     */
    String selectDefaultRegisterAvatar();

    /**
     * 查询系统配置列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param configName 配置名称
     * @param configKey  配置关键
     * @param configType 配置类型
     * @return {@link PageVO}<{@link SysConfigVO}>
     */
    PageVO<SysConfigVO> selectConfigList(Integer pageNum, Integer pageSize, String configName, String configKey, Integer configType);

    /**
     * 新增系统配置
     *
     * @param systemConfig 系统配置信息
     * @return {@link Long}
     */
    Long insertConfig(SystemConfig systemConfig);

    /**
     * 修改系统配置
     *
     * @param systemConfig 系统配置
     * @return boolean
     */
    boolean updateConfig(SystemConfig systemConfig);

    /**
     * 批量删除配置信息
     *
     * @param configIds 需要删除的配置ID
     */
    void deleteConfigByIds(Long[] configIds);

    /**
     * 清空配置缓存数据
     */
    void clearConfigCache();

    /**
     * 重置配置缓存数据
     */
    void resetConfigCache();

    /**
     * 校验配置键名是否唯一
     *
     * @param systemConfig 系统配置
     * @return boolean
     */
    boolean checkConfigKeyUnique(SystemConfig systemConfig);

    /**
     * 通过配置键获取系统配置
     * 由于配置键是唯一的，所以可以通过配置键获取系统配置
     *
     * @param systemConfigKey 系统配置关键
     * @return {@link SystemConfig}
     */
    SystemConfig getConfigByConfigKey(String systemConfigKey);
}

