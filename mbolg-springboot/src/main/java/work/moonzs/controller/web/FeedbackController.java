package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.annotation.WebOperationLogger;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.FeedBackDTO;
import work.moonzs.domain.entity.FeedBack;
import work.moonzs.service.FeedBackService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebFeedbackC")
@RequestMapping(value = "/web/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedBackService feedBackService;

    /**
     * 添加反馈
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加反馈")
    @WebOperationLogger(value = "反馈模块-添加反馈", type = "添加", desc = "用户反馈")
    @PostMapping("/add")
    public ResponseResult addWebFeedback(@Validated(value = {VG.Insert.class}) @RequestBody FeedBackDTO feedBackDTO) {
        feedBackService.addWebFeedback(BeanCopyUtil.copyBean(feedBackDTO, FeedBack.class));
        return ResponseResult.success();
    }
}
