package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.TagService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebTagC")
@RequestMapping(value = "/web/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * 获取标签列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取标签列表")
    @GetMapping("/list")
    public ResponseResult listWebTag() {
        return ResponseResult.success(tagService.listWebTag());
    }
}
