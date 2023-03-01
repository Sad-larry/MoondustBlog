package work.moonzs.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.FriendLinkDTO;
import work.moonzs.domain.entity.FriendLink;
import work.moonzs.service.FriendLinkService;

/**
 * @author Moondust月尘
 */
@RestController(value = "WebFriendLinkC")
@RequestMapping(value = "/web/friendlink")
@RequiredArgsConstructor
public class FriendLinkController {
    private final FriendLinkService friendlinkService;

    /**
     * 获取友链列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取友链列表")
    @GetMapping("/list")
    public ResponseResult listWebFriendLink() {
        return ResponseResult.successPageVO(friendlinkService.listWebFriendLink());
    }

    /**
     * 申请友链
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "申请友链")
    @PostMapping("/add")
    public ResponseResult addWebFriendLink(@Validated(VG.Insert.class) @RequestBody FriendLinkDTO friendLinkDTO) {
        friendlinkService.addWebFriendLink(BeanCopyUtil.copyBean(friendLinkDTO, FriendLink.class));
        return ResponseResult.success();
    }
}
