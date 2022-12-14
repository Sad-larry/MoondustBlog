package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.MessageDTO;
import work.moonzs.domain.entity.Message;
import work.moonzs.service.MessageService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebMessageC")
@RequestMapping(value = "/web/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    /**
     * 获取留言列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取留言列表")
    @GetMapping("/list")
    public ResponseResult listWebMessage() {
        return ResponseResult.success(messageService.listWebMessage());
    }

    /**
     * 添加留言
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加留言")
    @PostMapping("/add")
    public ResponseResult addWebMessage(@RequestBody MessageDTO messageDTO) {
        // TODO 设置留言 Ip 地址
        messageDTO.setIpAddress("");
        // TODO 设置留言 Ip 来源
        messageDTO.setIpSource("");
        messageService.addWebMessage(BeanCopyUtil.copyBean(messageDTO, Message.class));
        return ResponseResult.success();
    }
}
