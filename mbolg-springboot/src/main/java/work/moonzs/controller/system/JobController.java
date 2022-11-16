package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.JobDTO;
import work.moonzs.domain.entity.Job;
import work.moonzs.service.JobService;

/**
 * @author Moondust月尘
 */
@RestController("SystemJobC")
@RequestMapping("/system/job")
@RequiredArgsConstructor
public class JobController {
    // 		80202任务详情
    // 		80203添加任务
    // 		80204修改任务
    // 		80205删除任务
    // 		80206立即执行任务
    // 		80207修改任务状态
    // 		80208批量删除任务
    private final JobService jobService;

    /**
     * 定时任务列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取定时任务列表")
    @GetMapping("/list")
    public ResponseResult listJob(String jobName, String jobGroup, @RequestParam(name = "status", defaultValue = "1") Integer status) {
        return ResponseResult.successPageVO(jobService.listJob(jobName, jobGroup, status));
    }

    /**
     * 通过id查询任务
     *
     * @param jobId 任务id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询任务")
    @GetMapping("/{id}")
    public ResponseResult getJobById(@PathVariable("id") Long jobId) {
        return ResponseResult.success(jobService.getJobById(jobId));
    }

    /**
     * 添加任务
     *
     * @param jobDTO 任务dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加任务")
    @PostMapping
    public ResponseResult addJob(@Validated(VG.Insert.class) @RequestBody JobDTO jobDTO) {
        jobService.insertJob(BeanCopyUtil.copyBean(jobDTO, Job.class));
        return ResponseResult.success();
    }

    /**
     * 更新任务
     *
     * @param jobDTO 任务dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新任务")
    @PutMapping
    public ResponseResult updateJob(@Validated(VG.Update.class) @RequestBody JobDTO jobDTO) {
        jobService.updateJob(BeanCopyUtil.copyBean(jobDTO, Job.class));
        return ResponseResult.success();
    }

    /**
     * 删除任务
     *
     * @param jobIds 任务ids
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据任务id进行批量删除操作")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteJob(@PathVariable(value = "ids") Long[] jobIds) {
        jobService.deleteJob(jobIds);
        return ResponseResult.success();
    }
}
