package work.moonzs.base.exception;

import work.moonzs.base.enums.AppHttpCodeEnum;

/**
 * 负责抛出错误响应异常
 *
 * @author Moondust月尘
 */
public class ServiceException extends RuntimeException {
    private final int code;
    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ServiceException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
