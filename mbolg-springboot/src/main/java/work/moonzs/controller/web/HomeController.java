package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebHomeC")
@RequestMapping(value = "/web/home")
@RequiredArgsConstructor
public class HomeController {
    /**
     * 网站配置
     */
    public static final String WEBSITE = "webSite";
    /**
     * 统计数
     */
    public static final String COUNT = "count";
    /**
     * 网页标签图片
     */
    public static final String PAGELIST = "pageList";

    /**
     * 获取网站信息
     * <ul>
     *     <li>网站配置，例如博主头像，个人介绍等一些渲染在网页上的基本信息</li>
     *     <li>统计数，例如文章数，分类数等数据</li>
     *     <li>网页标签图片，例如对于主页有着主页展示的图片</li>
     * </ul>
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取网站信息")
    @GetMapping("/webSiteInfo")
    public ResponseResult getWebSiteInfo() {
        return ResponseResult.success()
                .put(WEBSITE, null)
                .put(COUNT, null)
                .put(PAGELIST, null);
    }
}
