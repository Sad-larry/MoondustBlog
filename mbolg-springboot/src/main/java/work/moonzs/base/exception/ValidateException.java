package work.moonzs.base.exception;

import work.moonzs.base.enums.AppHttpCodeEnum;

/**
 * 校验失败异常
 *
 * @author Moondust月尘
 */
public class ValidateException extends BaseException {

    public ValidateException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum);
    }

    public ValidateException(Integer code, String msg) {
        super(code, msg);
    }
}
