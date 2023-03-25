package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.JobLogDTO;
import work.moonzs.domain.entity.JobLog;
import work.moonzs.service.JobLogService;

/**
 * @author Moondust月尘
 */
@RestController("SystemJobLogC")
@RequestMapping("/system/jobLog")
@RequiredArgsConstructor
public class JobLogController {
    private final JobLogService jobLogService;

    /**
     * 获取定时任务日志列表
     *
     * @param pageNum   页面num
     * @param pageSize  页面大小
     * @param jobLogDTO 定时任务日志
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取定时任务日志列表")
    @GetMapping(value = "/list")
    @PreAuthorize("@ss.hasPermi('system:jobLog:list')")
    public ResponseResult listJobLog(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, JobLogDTO jobLogDTO) {
        return ResponseResult.successPageVO(jobLogService.listJobLog(pageNum, pageSize, BeanCopyUtil.copyBean(jobLogDTO, JobLog.class)));
    }

    /**
     * 删除定时任务日志
     *
     * @param ids id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除定时任务日志")
    @AdminOperationLogger(value = "删除定时任务日志")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:jobLog:delete')")
    public ResponseResult deleteJobLog(@PathVariable(value = "ids") Long[] ids) {
        jobLogService.deleteJobLog(ids);
        return ResponseResult.success();
    }

    /**
     * 清空日志列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "清空日志列表")
    @AdminOperationLogger(value = "清空日志列表")
    @GetMapping(value = "/clean")
    @PreAuthorize("@ss.hasPermi('system:jobLog:clean')")
    public ResponseResult cleanJobLog() {
        jobLogService.cleanJobLog();
        return ResponseResult.success();
    }
}
