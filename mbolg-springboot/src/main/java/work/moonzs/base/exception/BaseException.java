package work.moonzs.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import work.moonzs.base.enums.AppHttpCodeEnum;

/**
 * 基本的自定义异常
 *
 * @author Moondust月尘
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private final Integer code;
    private final String msg;

    public BaseException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum.getMsg());
        this.code = appHttpCodeEnum.getCode();
        this.msg = appHttpCodeEnum.getMsg();
    }

    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
