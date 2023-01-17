package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.WebConfigDTO;
import work.moonzs.domain.entity.WebConfig;
import work.moonzs.service.WebConfigService;

/**
 * @author Moondust月尘
 */
@RestController("SystemWebConfigC")
@RequestMapping("/system/webConfig")
@RequiredArgsConstructor
public class WebConfigController {
    private final WebConfigService webConfigService;

    /**
     * 获取网络配置
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取网络配置")
    @GetMapping
    public ResponseResult getWebConfig() {
        return ResponseResult.success(webConfigService.getWebConfig());
    }

    /**
     * 更新网站配置
     *
     * @param webConfigDTO 网络配置dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新网站配置")
    @AdminOperationLogger(value = "更新网站配置")
    @PutMapping
    public ResponseResult updateWebConfig(@Validated @RequestBody WebConfigDTO webConfigDTO) {
        webConfigService.updateWebConfig(BeanCopyUtil.copyBean(webConfigDTO, WebConfig.class));
        return ResponseResult.success();
    }
}
