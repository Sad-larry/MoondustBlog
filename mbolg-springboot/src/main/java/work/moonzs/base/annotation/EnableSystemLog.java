package work.moonzs.base.annotation;

import org.springframework.context.annotation.Import;
import work.moonzs.base.aspect.LogAspect;

import java.lang.annotation.*;

/**
 * 在配置类上启用 @SystemLog 自定义注解
 *
 * @author Moondust月尘
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogAspect.class)
public @interface EnableSystemLog {
}
