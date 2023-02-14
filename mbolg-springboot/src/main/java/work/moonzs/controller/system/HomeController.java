package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * 访问网站，增加浏览量
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/lineCount")
    public ResponseResult lineCount() {
        return ResponseResult.success(homeService.lineCount());
    }
}
