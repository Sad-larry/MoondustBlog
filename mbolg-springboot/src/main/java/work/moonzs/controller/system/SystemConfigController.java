package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.SystemConfigDTO;
import work.moonzs.domain.entity.SystemConfig;
import work.moonzs.service.SystemConfigService;

/**
 * @author Moondust月尘
 */
@RestController("SystemConfigC")
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SystemConfigController {
    private final SystemConfigService systemConfigService;

    /**
     * 获取系统配置列表
     */
    @SystemLog(businessName = "获取系统配置列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    public ResponseResult listSystemConfig(
            @RequestParam(defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = false) String configName,
            @RequestParam(required = false) String configKey,
            @RequestParam(required = false) Integer configType
    ) {
        return ResponseResult.successPageVO(systemConfigService.selectConfigList(pageNum, pageSize, configName, configKey, configType));
    }

    /**
     * 获取详细系统配置信息
     *
     * @param configId 配置id
     */
    @SystemLog(businessName = "获取详细系统配置信息")
    @GetMapping(value = "/{configId}")
    @PreAuthorize("@ss.hasPermi('system:config:info')")
    public ResponseResult getSystemConfigById(@PathVariable Long configId) {
        return ResponseResult.success(systemConfigService.selectConfigById(configId));
    }

    /**
     * 新增系统配置
     *
     * @param systemConfigDTO 系统配置dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "新增系统配置")
    @AdminOperationLogger(value = "新增系统配置")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    public ResponseResult addSystemConfig(@Validated(VG.Insert.class) @RequestBody SystemConfigDTO systemConfigDTO) {
        SystemConfig systemConfig = BeanCopyUtil.copyBean(systemConfigDTO, SystemConfig.class);
        // 判断是否存在相同的数据
        boolean isExist = systemConfigService.checkConfigKeyUnique(systemConfig);
        if (isExist) {
            return ResponseResult.fail(AppHttpCodeEnum.DATA_EXIST);
        }
        // 不存在则插入数据
        systemConfigService.insertConfig(systemConfig);
        return ResponseResult.success();
    }

    /**
     * 修改系统配置
     */
    @SystemLog(businessName = "修改系统配置")
    @AdminOperationLogger(value = "修改系统配置")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:config:update')")
    public ResponseResult updateSystemConfig(@Validated(VG.Update.class) @RequestBody SystemConfigDTO systemConfigDTO) {
        SystemConfig systemConfig = BeanCopyUtil.copyBean(systemConfigDTO, SystemConfig.class);
        boolean isExist = systemConfigService.checkConfigKeyUnique(systemConfig);
        if (isExist) {
            return ResponseResult.fail(AppHttpCodeEnum.DATA_EXIST);
        }
        systemConfigService.updateConfig(systemConfig);
        return ResponseResult.success();
    }

    /**
     * 删除系统配置
     */
    @SystemLog(businessName = "删除系统配置")
    @AdminOperationLogger(value = "删除系统配置")
    @DeleteMapping("/{configIds}")
    @PreAuthorize("@ss.hasPermi('system:config:delete')")
    public ResponseResult deleteSystemConfig(@PathVariable Long[] configIds) {
        systemConfigService.deleteConfigByIds(configIds);
        return ResponseResult.success();
    }

    /**
     * 查询指定配置键名的配置值
     *
     * @param configKey 配置key
     */
    @SystemLog(businessName = "查询指定配置键名的配置值")
    @GetMapping(value = "/configKey/{configKey}")
    @PreAuthorize("@ss.hasPermi('system:config:configKey')")
    public ResponseResult getSystemConfigByConfigKey(@PathVariable String configKey) {
        return ResponseResult.success(systemConfigService.selectConfigByKey(configKey));
    }

    /**
     * 刷新配置缓存
     */
    @SystemLog(businessName = "刷新配置缓存")
    @AdminOperationLogger(value = "刷新配置缓存")
    @GetMapping("/refreshCache")
    @PreAuthorize("@ss.hasPermi('system:config:refreshCache')")
    public ResponseResult refreshCache() {
        systemConfigService.resetConfigCache();
        return ResponseResult.success();
    }
}
