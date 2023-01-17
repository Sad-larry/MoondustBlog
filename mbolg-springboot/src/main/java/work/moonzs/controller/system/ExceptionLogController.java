package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.ExceptionLogService;

/**
 * @author Moondust月尘
 */
@RestController("SystemExceptionLogC")
@RequestMapping("/system/exceptionLog")
public class ExceptionLogController {
    @Autowired
    private ExceptionLogService exceptionLogService;

    /**
     * 异常日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "异常日志列表")
    @GetMapping("/list")
    public ResponseResult listExceptionLog(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseResult.success(exceptionLogService.listExceptionLog(pageNum, pageSize));
    }

    /**
     * 删除异常日志
     *
     * @param exceptionLogIds 异常日志id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据异常日志id进行批量删除异常")
    @AdminOperationLogger(value = "删除异常日志")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteExceptionLog(@PathVariable(value = "ids") Long[] exceptionLogIds) {
        exceptionLogService.deleteExceptionLog(exceptionLogIds);
        return ResponseResult.success();
    }
}
