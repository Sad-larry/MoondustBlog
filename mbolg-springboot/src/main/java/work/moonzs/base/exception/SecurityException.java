package work.moonzs.base.exception;

import lombok.Getter;
import work.moonzs.base.enums.AppHttpCodeEnum;

/**
 * SpringSecurity异常，token异常
 *
 * @author Moondust月尘
 */
@Getter
public class SecurityException extends BaseException {
    public SecurityException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum);
    }

    public SecurityException(Integer code, String msg) {
        super(code, msg);
    }
}
