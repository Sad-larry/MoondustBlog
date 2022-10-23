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

    public ServiceException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum);
    }

    public ServiceException(Integer code, String msg) {
        super(code, msg);
    }
}
