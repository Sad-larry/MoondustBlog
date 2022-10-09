package work.moonzs.base.annotation;

import work.moonzs.base.validate.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Moondust月尘
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface UsernameValid {
    String message() default "username 必须为 md";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
