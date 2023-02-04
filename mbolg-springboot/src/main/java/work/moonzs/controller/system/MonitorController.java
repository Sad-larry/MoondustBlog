package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult redisMonitor() {
        return ResponseResult.success(homeService.getCacheInfo());
    }

    /**
     * 德鲁伊统计
     *
     * @return {@link Object}
     */
    // @GetMapping("/druid/stat")
    // public Object druidStat() {
    //     // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
    //     return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    // }
}
