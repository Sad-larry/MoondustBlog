package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.HomeService;

/**
 * @author Moondust月尘
 */
@RestController("SystemHomeC")
@RequestMapping("/system/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    /**
     * 后台首页图表数据
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "后台首页图表数据")
    @GetMapping("/lineCount")
    public ResponseResult lineCount() {
        return ResponseResult.success(homeService.lineCount());
    }

    /**
     * 初始化首页图表数据
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "初始化首页图表数据")
    @GetMapping("/init")
    public ResponseResult initChart() {
        return ResponseResult.success(homeService.initChart());
    }
}
