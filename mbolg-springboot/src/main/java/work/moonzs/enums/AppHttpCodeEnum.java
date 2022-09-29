package work.moonzs.enums;

/**
 * @author Moondust月尘
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 失败
    SERVER_INNER_ERR(500, "系统繁忙"),

    // 字段不能为空
    FIELD_EMPTY(45001, "字段不能为空"),
    // 用户名不存在
    USER_NOT_EXIST(45002, "用户不存在"),
    // 分类不存在
    CATEGORY_NOT_EXIST(45003, "分类不存在"),
    // 标签不存在
    TAG_NOT_EXIST(45004, "标签不存在"),
    // 标签已存在
    TAG_EXIST(45005, "标签已存在"),
    // 分类已存在
    CATEGORY_EXIST(45006, "分类已存在"),

    // 警告：文章未添加标签
    WARNING_TAG_EMPTY(54001, "警告：文章未添加标签");

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
