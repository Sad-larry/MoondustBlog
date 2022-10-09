package work.moonzs.base.exception;

/**
 * 校验失败异常
 *
 * @author Moondust月尘
 */
public class ValidateException extends RuntimeException {
    /**
     * 无参构造函数
     */
    public ValidateException() {
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public ValidateException(String message) {
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个新的异常
     */
    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 用指定原因构造一个新的异常
     */
    public ValidateException(Throwable cause) {
        super(cause);
    }
}
