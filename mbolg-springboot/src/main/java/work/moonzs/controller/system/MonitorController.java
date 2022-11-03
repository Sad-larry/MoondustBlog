package work.moonzs.controller.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moondust月尘
 */
@RestController("MonitorC")
@RequestMapping("/monitor")
public class MonitorController {
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
