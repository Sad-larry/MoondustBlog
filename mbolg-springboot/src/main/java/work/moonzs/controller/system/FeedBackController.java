package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.FeedBackDTO;
import work.moonzs.domain.entity.FeedBack;
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
    @PreAuthorize("@ss.hasPermi('system:feedback:list')")
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
    @PreAuthorize("@ss.hasPermi('system:feedback:delete')")
    public ResponseResult deleteFeedBack(@PathVariable("ids") Long[] feedBackIds) {
        feedBackService.deleteFeedBack(feedBackIds);
        return ResponseResult.success();
    }

    /**
     * 回复反馈
     *
     * @param feedBackDTO 回复反馈dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "回复反馈")
    @AdminOperationLogger(value = "回复反馈")
    @PostMapping("/reply")
    @PreAuthorize("@ss.hasPermi('system:feedback:reply')")
    public ResponseResult replyFeedBack(@Validated(value = {VG.Update.class}) @RequestBody FeedBackDTO feedBackDTO) {
        FeedBack feedBack = BeanCopyUtil.copyBean(feedBackDTO, FeedBack.class);
        String replyContent = feedBackDTO.getReplyContent();
        feedBackService.replyFeedBack(feedBack, replyContent);
        return ResponseResult.success();
    }
}
