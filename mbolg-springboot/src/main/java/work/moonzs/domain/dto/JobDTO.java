package work.moonzs.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.base.annotation.CronExpressionValid;
import work.moonzs.base.validate.VG;

import javax.validation.constraints.*;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "定时任务DTO")
public class JobDTO {
    /**
     * 任务ID
     */
    @ApiModelProperty(notes = "任务ID")
    @Null(message = "任务ID必须为空", groups = VG.Insert.class)
    @NotNull(message = "任务ID不能为空", groups = VG.Update.class)
    private Long jobId;
    /**
     * 任务名称
     */
    @ApiModelProperty(notes = "任务名称")
    @NotBlank(message = "任务名称不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String jobName;
    /**
     * 任务组名
     */
    @ApiModelProperty(notes = "任务组名")
    @NotBlank(message = "任务组名不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String jobGroup;
    /**
     * 调用目标字符串
     */
    @ApiModelProperty(notes = "调用目标字符串")
    @NotBlank(message = "调用目标字符串不能为空", groups = {VG.Insert.class, VG.Update.class})
    private String invokeTarget;
    /**
     * cron执行表达式
     * 自定义注解验证
     */
    @ApiModelProperty(notes = "cron执行表达式")
    @CronExpressionValid(groups = {VG.Insert.class, VG.Update.class})
    private String cronExpression;
    /**
     * 计划执行错误策略(1立即执行,2执行一次,3放弃执行)
     */
    @ApiModelProperty(notes = "计划执行错误策略(1立即执行,2执行一次,3放弃执行)")
    @Min(value = 1, message = "计划执行错误策略设置只有1、2、3", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 3, message = "计划执行错误策略设置只有1、2、3", groups = {VG.Insert.class, VG.Update.class})
    private Integer misfirePolicy;
    /**
     * 是否并发执行(0禁止,1允许)
     */
    @ApiModelProperty(notes = "是否并发执行(0禁止,1允许)")
    @Min(value = 0, message = "是否并发执行设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "是否并发执行设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer concurrent;
    /**
     * 任务状态(0暂停,1正常)
     */
    @ApiModelProperty(notes = "任务状态(0暂停,1正常)")
    @Min(value = 0, message = "任务状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    @Max(value = 1, message = "任务状态设置只有0、1", groups = {VG.Insert.class, VG.Update.class})
    private Integer status;
    /**
     * 备注信息
     */
    @ApiModelProperty(notes = "备注信息")
    private String remark;
}
