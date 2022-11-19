package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.base.validate.VG2;
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
    private final JobService jobService;

    /**
     * 定时任务列表
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取定时任务列表")
    @GetMapping("/list")
    public ResponseResult listJob(String jobName, String jobGroup, Integer status) {
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
    public ResponseResult addJob(@Validated(VG.Insert.class) @RequestBody JobDTO jobDTO) throws SchedulerException {
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
    public ResponseResult updateJob(@Validated(VG.Update.class) @RequestBody JobDTO jobDTO) throws SchedulerException {
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
    public ResponseResult deleteJob(@PathVariable(value = "ids") Long[] jobIds) throws SchedulerException {
        jobService.deleteJob(jobIds);
        return ResponseResult.success();
    }

    /**
     * 立即执行一次任务
     * 任务ID以及任务组名不能为空
     *
     * @param jobDTO 任务dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "立即执行一次任务")
    @PostMapping("/run")
    public ResponseResult runJob(@Validated(VG.Select.class) @RequestBody JobDTO jobDTO) throws SchedulerException {
        jobService.runJob(BeanCopyUtil.copyBean(jobDTO, Job.class));
        return ResponseResult.success();
    }

    /**
     * 立即暂停任务
     * 任务ID以及任务组名不能为空
     *
     * @param jobDTO 任务dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "立即暂停任务")
    @PostMapping("/pause")
    public ResponseResult pauseJob(@Validated(VG.Select.class) @RequestBody JobDTO jobDTO) throws SchedulerException {
        jobService.pauseJob(BeanCopyUtil.copyBean(jobDTO, Job.class));
        return ResponseResult.success();
    }

    /**
     * 修改任务状态
     *
     * @param jobDTO 任务dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "修改任务状态")
    @PutMapping("/change")
    public ResponseResult changeJobStatus(@Validated(VG2.Update2.class) @RequestBody JobDTO jobDTO) throws SchedulerException {
        jobService.changeJobStatus(BeanCopyUtil.copyBean(jobDTO, Job.class));
        return ResponseResult.success();
    }
}
