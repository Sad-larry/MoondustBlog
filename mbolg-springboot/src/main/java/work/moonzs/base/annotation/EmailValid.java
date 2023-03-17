package work.moonzs.base.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotBlank
@Email
@Documented
public @interface EmailValid {
    String message() default "邮箱地址格式不正确!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
