package work.moonzs.base.annotation;

import work.moonzs.base.validate.CronExpressionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CronExpressionValidator.class)
@Documented
public @interface CronExpressionValid {
    String message() default "非法的Cron表达式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
