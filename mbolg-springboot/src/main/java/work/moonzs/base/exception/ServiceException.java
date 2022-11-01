package work.moonzs.base.exception;

import lombok.Getter;
import work.moonzs.base.enums.AppHttpCodeEnum;

/**
 * 负责抛出服务异常
 *
 * @author Moondust月尘
 */
@Getter
public class ServiceException extends BaseException {
    /**
     * 服务错误代码
     */
    public static final Integer SERVICE_ERROR_CODE = 500;

    public ServiceException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum);
    }

    public ServiceException(String msg) {
        super(SERVICE_ERROR_CODE, msg);
    }

    public ServiceException(Integer code, String msg) {
        super(code, msg);
    }
}
