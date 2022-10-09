package work.moonzs.base.annotation;

import org.springframework.context.annotation.Import;
import work.moonzs.base.aspect.ParamInspect;

import java.lang.annotation.*;

/**
 * 在配置类上启用@ParamCheck自定义注解
 *
 * @author Moondust月尘
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ParamInspect.class})
public @interface EnableParamCheck {
}
