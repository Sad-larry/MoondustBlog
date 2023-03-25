package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.HomeService;

/**
 * @author Moondust月尘
 */
@RestController("SystemMonitorC")
@RequestMapping("/system/monitor")
public class MonitorController {
    @Autowired
    private HomeService homeService;

    /**
     * redis缓存监控
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "redis缓存监控")
    @GetMapping("/cache")
    @PreAuthorize("@ss.hasPermi('system:monitor:cache')")
    public ResponseResult redisMonitor() {
        return ResponseResult.success(homeService.getCacheInfo());
    }
}
