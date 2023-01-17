package work.moonzs.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录后台管理员操作的日志
 *
 * @author Moondust月尘
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOperationLogger {
    /**
     * 操作名字
     */
    String value() default "";

    /**
     * 是否保存到数据库，默认为 true
     */
    boolean saveDatabase() default true;
}
