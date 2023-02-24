package work.moonzs.base.web.common;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.exception.ServiceException;

import java.util.Collection;

/**
 * 业务断言
 */
public final class BusinessAssert {

    /**
     * 断言一个boolean表达式，判断表达式是否为真，
     * 如果 expression 为 true 则说明断言成功，不抛出异常信息
     * 否则 expression 为 false，说明断言失败，负责抛出失败信息
     *
     * @param expression boolean表达式
     * @param message    不满足断言的异常信息
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }

    public static void state(boolean expression, AppHttpCodeEnum codeEnum) {
        if (!expression) {
            throw new ServiceException(codeEnum);
        }
    }

    /**
     * 断言对象不为空，
     * 如果对象不为空，说明断言成功，对象确实不为空，不抛出异常
     *
     * @param object 对象
     * @param msg    不满足断言的异常信息
     */
    public static void notNull(Object object, String msg) {
        state(object != null, msg);
    }

    public static void notNull(Object object, AppHttpCodeEnum codeEnum) {
        state(object != null, codeEnum);
    }

    /**
     * 断言字符串不为空
     *
     * @param str 字符串
     * @param msg 不满足断言的异常信息
     */
    public static void notEmpty(String str, String msg) {
        state(!StringUtils.hasText(str), msg);
    }

    public static void notEmpty(String str, AppHttpCodeEnum codeEnum) {
        state(!StringUtils.hasText(str), codeEnum);
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void notEmpty(Collection<?> collection, String msg) {
        state(!CollectionUtils.isEmpty(collection), msg);
    }

    public static void notEmpty(Collection<?> collection, AppHttpCodeEnum codeEnum) {
        state(!CollectionUtils.isEmpty(collection), codeEnum);
    }

    /**
     * 直接断言失败，抛出异常信息
     *
     * @param message 消息
     */
    public static void fail(String message) {
        state(false, message);
    }

    public static void fail(AppHttpCodeEnum codeEnum) {
        state(false, codeEnum);
    }

    /**
     * 断言不满足条件，一旦满足条件，即bool为true，则断言失败，负责抛出异常信息
     *
     * @param message 消息
     */
    public static void isFalse(boolean bool, String message) {
        state(!bool, message);
    }

    public static void isFalse(boolean bool, AppHttpCodeEnum codeEnum) {
        state(!bool, codeEnum);
    }

    public static void isTure(boolean bool, String message) {
        state(bool, message);
    }

    public static void isTure(boolean bool, AppHttpCodeEnum codeEnum) {
        state(bool, codeEnum);
    }

    /**
     * 断言对象不相等，如果两个对象相等，则抛出异常
     *
     * @param code     代码
     * @param captcha  验证码
     * @param codeEnum 枚举代码
     */
    public static void notEquals(String code, String captcha, AppHttpCodeEnum codeEnum) {
        notEquals(code, captcha, false, codeEnum);
    }

    public static void notEquals(String code, String captcha, boolean ignoreCase, AppHttpCodeEnum codeEnum) {
        if (ignoreCase) {
            state(!code.equalsIgnoreCase(captcha), codeEnum);
        } else {
            state(!code.equals(captcha), codeEnum);
        }
    }

    /**
     * 断言两个字符串是否相等，如果不相等，则抛出异常
     *
     * @param code       代码
     * @param captcha    验证码
     * @param ignoreCase 忽略大小写
     * @param codeEnum   枚举代码
     */
    public static void equals(String code, String captcha, boolean ignoreCase, AppHttpCodeEnum codeEnum) {
        if (ignoreCase) {
            state(code.equalsIgnoreCase(captcha), codeEnum);
        } else {
            state(code.equals(captcha), codeEnum);
        }
    }

    public static void equals(String code, String captcha, AppHttpCodeEnum codeEnum) {
        equals(code, captcha, false, codeEnum);
    }
}
