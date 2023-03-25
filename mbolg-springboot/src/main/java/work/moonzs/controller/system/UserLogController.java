package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.UserLogService;

/**
 * @author Moondust月尘
 */
@RestController("SystemUserLogC")
@RequestMapping("/system/userLog")
public class UserLogController {
    @Autowired
    private UserLogService userLogService;

    /**
     * 用户日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "用户日志列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:userLog:list')")
    public ResponseResult listUserLog(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(userLogService.listUserLog(pageNum, pageSize));
    }

    /**
     * 删除用户日志
     *
     * @param userLogIds 用户日志id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据用户日志id进行批量删除用户")
    @AdminOperationLogger(value = "删除用户日志")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:userLog:delete')")
    public ResponseResult deleteUserLog(@PathVariable(value = "ids") Long[] userLogIds) {
        userLogService.deleteUserLog(userLogIds);
        return ResponseResult.success();
    }
}
