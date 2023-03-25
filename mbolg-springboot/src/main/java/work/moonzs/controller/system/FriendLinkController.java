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
import work.moonzs.domain.dto.FriendLinkDTO;
import work.moonzs.domain.entity.FriendLink;
import work.moonzs.service.FriendLinkService;

/**
 * @author Moondust月尘
 */
@RestController("SystemFriendLinkC")
@RequestMapping("/system/friendlink")
@RequiredArgsConstructor
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    /**
     * 友链列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param name     名字
     * @param status   状态
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "友链列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:friendlink:list')")
    public ResponseResult listFriendLink(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(required = false) String name, @RequestParam(required = false) Integer status) {
        return ResponseResult.successPageVO(friendLinkService.listFriendLink(pageNum, pageSize, name, status));
    }

    /**
     * 添加友链
     *
     * @param friendLinkDTO 友链dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加友链")
    @AdminOperationLogger(value = "添加友链")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:friendlink:add')")
    public ResponseResult addFriendLink(@Validated(VG.Insert.class) @RequestBody FriendLinkDTO friendLinkDTO) {
        friendLinkService.insertFriendLink(BeanCopyUtil.copyBean(friendLinkDTO, FriendLink.class));
        return ResponseResult.success();
    }

    /**
     * 更新友链
     *
     * @param friendLinkDTO 友链dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新友链")
    @AdminOperationLogger(value = "更新友链")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:friendlink:update')")
    public ResponseResult updateFriendLink(@Validated(VG.Update.class) @RequestBody FriendLinkDTO friendLinkDTO) {
        friendLinkService.updateFriendLink(BeanCopyUtil.copyBean(friendLinkDTO, FriendLink.class));
        return ResponseResult.success();
    }

    /**
     * 删除友链
     *
     * @param friendLinkIds 友链id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据友链id进行批量删除操作")
    @AdminOperationLogger(value = "删除友链")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:friendlink:delete')")
    public ResponseResult deleteFriendLink(@PathVariable(value = "ids") Long[] friendLinkIds) {
        friendLinkService.deleteFriendLink(friendLinkIds);
        return ResponseResult.success();
    }

    /**
     * 友链置顶
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "友链置顶")
    @AdminOperationLogger(value = "友链置顶")
    @PostMapping("/top")
    @PreAuthorize("@ss.hasPermi('system:friendlink:top')")
    public ResponseResult topFriendLink(@Validated @RequestBody FriendLinkDTO friendLinkDTO) {
        Long friendLinkId = friendLinkDTO.getId();
        friendLinkService.topFriendLink(friendLinkId);
        return ResponseResult.success();
    }

    /**
     * 审核通过友链
     *
     * @param friendLinkIds 消息id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "审核通过友链")
    @AdminOperationLogger(value = "审核通过友链")
    @GetMapping("/pass/{ids}")
    @PreAuthorize("@ss.hasPermi('system:friendlink:pass')")
    public ResponseResult passFriendLink(@PathVariable("ids") Long[] friendLinkIds) {
        friendLinkService.passFriendLink(friendLinkIds);
        return ResponseResult.success();
    }
}
