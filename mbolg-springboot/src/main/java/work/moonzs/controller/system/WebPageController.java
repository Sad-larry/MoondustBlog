package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.WebPageDTO;
import work.moonzs.domain.entity.WebPage;
import work.moonzs.service.WebPageService;

/**
 * @author Moondust月尘
 */
@RestController("SystemWebPageC")
@RequestMapping("/system/webPage")
public class WebPageController {
    @Autowired
    private WebPageService webPageService;

    /**
     * 页面列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取页面列表")
    @GetMapping("/list")
    public ResponseResult listWebPage() {
        return ResponseResult.success(webPageService.listWebPage());
    }

    /**
     * 添加页面
     *
     * @param webPageDTO 页面dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加页面")
    @PostMapping
    public ResponseResult addWebPage(@Validated(VG.Insert.class) @RequestBody WebPageDTO webPageDTO) {
        return ResponseResult.success(webPageService.insertWebPage(BeanCopyUtil.copyBean(webPageDTO, WebPage.class)));
    }


    /**
     * 更新页面
     *
     * @param webPageDTO 页面dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新页面")
    @PutMapping
    public ResponseResult updateWebPage(@Validated(VG.Update.class) @RequestBody WebPageDTO webPageDTO) {
        webPageService.updateWebPage(BeanCopyUtil.copyBean(webPageDTO, WebPage.class));
        return ResponseResult.success();
    }

    /**
     * 删除页面
     *
     * @param webPageId 页面id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除指定ID页面")
    @DeleteMapping("/{id}")
    public ResponseResult deleteWebPage(@PathVariable(value = "id") Long webPageId) {
        webPageService.deleteWebPage(webPageId);
        return ResponseResult.success();
    }
}
