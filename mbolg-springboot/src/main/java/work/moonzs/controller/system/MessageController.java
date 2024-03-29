package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.MessageService;

/**
 * @author Moondust月尘
 */
@RestController("SystemMessageC")
@RequestMapping("/system/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    /**
     * 留言列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "留言列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:message:list')")
    public ResponseResult listMessage(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return ResponseResult.success(messageService.listMessage(pageNum, pageSize, fuzzyField));
    }

    /**
     * 审核通过留言
     *
     * @param messageIds 消息id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "审核通过留言")
    @AdminOperationLogger(value = "审核通过留言")
    @GetMapping("/pass/{ids}")
    @PreAuthorize("@ss.hasPermi('system:message:pass')")
    public ResponseResult passMessage(@PathVariable("ids") Long[] messageIds) {
        messageService.passMessage(messageIds);
        return ResponseResult.success();
    }

    /**
     * 删除消息
     *
     * @param messageIds 消息id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据留言id进行批量删除操作")
    @AdminOperationLogger(value = "删除消息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:message:delete')")
    public ResponseResult deleteMessage(@PathVariable("ids") Long[] messageIds) {
        messageService.deleteMessage(messageIds);
        return ResponseResult.success();
    }
}
