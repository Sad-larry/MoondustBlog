package work.moonzs.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.SystemConfig;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysConfigVO;
import work.moonzs.mapper.SystemConfigMapper;
import work.moonzs.service.SystemConfigService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 系统配置表(SystemConfig)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:27
 */
@Service("systemConfigService")
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    @Override
    public void loadingConfigCache() {
        List<SystemConfig> list = list();
        list.forEach(systemConfig -> {
            // 在缓存中设置数据
            redisCache.set(getCacheKey(systemConfig.getConfigKey()), systemConfig.getConfigValue());
        });
    }

    @Override
    public SysConfigVO selectConfigById(Long configId) {
        return BeanCopyUtil.copyBean(getById(configId), SysConfigVO.class);
    }

    @Override
    public String selectConfigByKey(String configKey) {
        // 先查询缓存中是否有对应的值
        String configValue = (String) redisCache.get(getCacheKey(configKey));
        if (StrUtil.isNotBlank(configValue)) {
            return configValue;
        }
        // 如果缓存中没有则去数据库中查询
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SystemConfig::getId, SystemConfig::getConfigKey, SystemConfig::getConfigValue).eq(SystemConfig::getConfigKey, configKey).last(StatusConstants.LIMIT_ONE);
        // 没有数据不报异常
        SystemConfig one = getOne(queryWrapper, false);
        return one.getConfigValue();
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        // 若没找到相对应的value，则采用默认值，默认为 true，需要验证码验证
        if (StrUtil.isBlank(captchaEnabled)) {
            return true;
        }
        // 将字符串类型转换成 boolean 类型
        Boolean enabled = Convert.toBool(captchaEnabled);
        // 如果转换失败，则返回 false
        if (null == enabled) {
            return false;
        }
        return enabled;
    }

    @Override
    public PageVO<SysConfigVO> selectConfigList(Integer pageNum, Integer pageSize, String configName, String configKey, Integer configType) {
        Page<SystemConfig> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(configName), SystemConfig::getConfigName, configName)
                .like(StrUtil.isNotBlank(configKey), SystemConfig::getConfigKey, configKey)
                .eq(ObjUtil.isNotNull(configType), SystemConfig::getConfigType, configType);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysConfigVO.class), page.getTotal());
    }

    @Override
    public Long insertConfig(SystemConfig systemConfig) {
        int row = baseMapper.insert(systemConfig);
        if (row > 0) {
            // 插入数据库成功的数据添加到缓存中
            redisCache.set(getCacheKey(systemConfig.getConfigKey()), systemConfig.getConfigValue());
        }
        return systemConfig.getId();
    }

    @Override
    public boolean updateConfig(SystemConfig systemConfig) {
        boolean update = updateById(systemConfig);
        if (update) {
            // 修改成功的数据同时也修改缓存中的数据
            redisCache.set(getCacheKey(systemConfig.getConfigKey()), systemConfig.getConfigValue());
        }
        return update;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SystemConfig byId = getById(configId);
            if (byId == null) {
                continue;
            }
            if (StatusConstants.NORMAL.equals(byId.getConfigType())) {
                BusinessAssert.fail(String.format("内置参数【%1$s】不能删除", byId.getConfigKey()));
            }
            removeById(configId);
            // 删除缓存中的数据
            redisCache.del(getCacheKey(byId.getConfigKey()));
        }
    }

    @Override
    public void clearConfigCache() {
        // 查询前缀为 sys:config: 的键名
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.del(keys);
    }

    @Override
    public void resetConfigCache() {
        // 先清空缓存中的数据
        clearConfigCache();
        // 再从数据库中重新加载数据到缓存中
        loadingConfigCache();
    }

    @Override
    public boolean checkConfigKeyUnique(SystemConfig systemConfig) {
        LambdaQueryWrapper<SystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConfig::getConfigKey, systemConfig.getConfigKey());
        // 查询 configkey 的数量是否大于1
        return count(queryWrapper) > 1;
    }

    /**
     * 设置 cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}

