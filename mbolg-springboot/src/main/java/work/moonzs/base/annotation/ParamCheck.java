package work.moonzs.base.annotation;

import java.lang.annotation.*;

/**
 * 自定义参数校验注解，解决对象嵌套校验问题
 *
 * @author Moondust月尘
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {
    /**
     * 要校验的类
     */
    Class[] clazzs() default {};

    /**
     * 校验参数名
     */
    String[] params() default {};

    /**
     * 严格分组，如果不写，那么校验属性上的校验注解也不应该带
     */
    Class[] group() default {};
}
