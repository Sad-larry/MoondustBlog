package work.moonzs.base.validate;

import work.moonzs.base.annotation.CronExpressionValid;
import work.moonzs.base.quartz.CronUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证Cron表达式是否正确
 *
 * @author Moondust月尘
 */
public class CronExpressionValidator implements ConstraintValidator<CronExpressionValid, String> {
    @Override
    public boolean isValid(String cronExpression, ConstraintValidatorContext constraintValidatorContext) {
        // 如果cronExpression为空，说明该字段没有值，或者不修改
        if (cronExpression == null) {
            return true;
        }
        // 当cronExpression有值时，即使为空串，都验证不通过
        return CronUtil.idValid(cronExpression);
    }
}
