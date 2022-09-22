package work.moonzs.enums;

/**
 * @author Moondust月尘
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 失败
    SERVER_INNER_ERR(500, "系统繁忙");
    final Integer code;
    final String msg;

    AppHttpCodeEnum(Integer code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
