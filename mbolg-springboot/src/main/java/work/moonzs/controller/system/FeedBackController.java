package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.FeedBackService;

/**
 * @author Moondust月尘
 */
@RestController("SystemFeedBackC")
@RequestMapping("/system/feedback")
@RequiredArgsConstructor
public class FeedBackController {
    private final FeedBackService feedBackService;

    /**
     * 反馈列表
     *
     * @param type 类型
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "反馈列表")
    @GetMapping("/list")
    public ResponseResult listFeedBack(@RequestParam(defaultValue = "", required = false) Integer type) {
        return ResponseResult.success(feedBackService.listFeedBack(type));
    }

    /**
     * 删除反馈
     *
     * @param feedBackIds 反馈id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据反馈id进行批量删除操作")
    @AdminOperationLogger(value = "删除反馈")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteFeedBack(@PathVariable("ids") Long[] feedBackIds) {
        feedBackService.deleteFeedBack(feedBackIds);
        return ResponseResult.success();
    }
}
