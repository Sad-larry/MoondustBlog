package work.moonzs.base.enums;

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
    // 角色不存在
    ROLE_NOT_EXIST(45007, "角色不存在"),
    // 菜单不存在
    MENU_NOT_EXIST(45008, "菜单不存在"),
    // 菜单已存在
    MENU_EXIST(45009, "菜单已存在"),
    // 角色已存在
    ROLE_EXIST(45010, "角色已存在"),
    // 文件类型不匹配
    FILE_TYPE_NOT_MATCH(45011, "文件类型不匹配"),
    // 用户密码错误
    USER_FAILED_CERTIFICATION(45012, "用户密码错误"),
    // 用户未登录
    USER_NEED_LOGIN(45013, "用户未登录"),
    // TOKEN异常
    TOKEN_ABNORMAL(45014, "TOKEN异常"),
    // 权限不足
    NO_OPERATOR_AUTH(45015, "权限不足"),

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