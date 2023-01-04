package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.OperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.AdminLogService;

/**
 * @author Moondust月尘
 */
@RestController("SystemAdminLogC")
@RequestMapping("/system/adminLog")
public class AdminLogController {
    @Autowired
    private AdminLogService adminLogService;

    /**
     * 操作日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "操作日志列表")
    @GetMapping("/list")
    public ResponseResult listAdminLog(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(adminLogService.listAdminLog(pageNum, pageSize));
    }

    /**
     * 删除操作日志
     *
     * @param adminLogIds 操作日志id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据操作日志id进行批量删除操作")
    @OperationLogger(value = "删除操作日志")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteAdminLog(@PathVariable(value = "ids") Long[] adminLogIds) {
        adminLogService.deleteAdminLog(adminLogIds);
        return ResponseResult.success();
    }
}
