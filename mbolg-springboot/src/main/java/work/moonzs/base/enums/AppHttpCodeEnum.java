package work.moonzs.base.enums;

/**
 * @author Moondust月尘
 */
public enum AppHttpCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 失败
     */
    SERVER_INNER_ERR(500, "系统繁忙"),
    /**
     * HTTP请求方法不支持
     */
    HTTP_REQUESTMETHOD_NOT_SUPPORTED(405, "HTTP请求方法不支持"),
    /**
     * 非法请求
     */
    BAD_REQUEST(400, "非法请求"),
    /**
     * 请求参数缺失
     */
    REQUIRED_REQUEST_PARAMETER(400, "请求参数缺失"),
    /**
     * 不支持的媒体参数类型
     */
    MEDIATYPE_NOT_SUPPORTED(415, "不支持的媒体参数类型"),

    /**
     * 字段不能为空
     */
    FIELD_EMPTY(45001, "字段不能为空"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(45002, "用户不存在"),
    /**
     * 分类不存在
     */
    CATEGORY_NOT_EXIST(45003, "分类不存在"),
    /**
     * 标签不存在
     */
    TAG_NOT_EXIST(45004, "标签不存在"),
    /**
     * 标签已存在
     */
    TAG_EXIST(45005, "标签已存在"),
    /**
     * 分类已存在
     */
    CATEGORY_EXIST(45006, "分类已存在"),
    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST(45007, "角色不存在"),
    /**
     * 菜单不存在
     */
    MENU_NOT_EXIST(45008, "菜单不存在"),
    /**
     * 菜单已存在
     */
    MENU_EXIST(45009, "菜单已存在"),
    /**
     * 角色已存在
     */
    ROLE_EXIST(45010, "角色已存在"),
    /**
     * 文件类型不匹配
     */
    FILE_TYPE_NOT_MATCH(45011, "文件类型不匹配"),
    /**
     * 用户密码错误
     */
    USER_FAILED_CERTIFICATION(45012, "用户密码错误"),
    /**
     * 用户未登录或Token失效
     */
    USER_NEED_LOGIN(45013, "用户未登录"),
    /**
     * TOKEN异常
     */
    TOKEN_ABNORMAL(45014, "TOKEN异常"),
    /**
     * 权限不足
     */
    NO_OPERATOR_AUTH(45015, "权限不足"),
    /**
     * 非法登录
     */
    ILLEGAL_LOGIN(45016, "非法登录"),
    /**
     * 无效登录
     */
    INVALID_LOGIN(45017, "无效登录"),
    /**
     * 请求体缺失
     */
    REQUIRED_REQUEST_BODY(45018, "请求体缺失"),
    /**
     * 验证码有误
     */
    CAPTCHA_FAIL(45019, "验证码有误"),
    /**
     * 账号已在别的地方登录，请重新登录
     */
    REPEAT_LOGIN(45020, "账号已在别的地方登录，请重新登录"),
    /**
     * token已过期
     */
    TOKEN_OVERDUE(45021, "TOKEN 已过期"),
    /**
     * 文件内容为空
     */
    FILE_IS_EMPTY(45022, "文件内容为空"),
    /**
     * 文件上传成功
     */
    FILE_UPLOAD_SUCCESS(45023, "文件上传成功"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAIL(45024, "文件上传失败"),
    /**
     * 系统找不到指定文件或路径
     */
    NOT_FIND_SPECIFIED_PATH(45025, "系统找不到指定文件或路径"),
    /**
     * Token 已过期
     */
    TOKEN_EXPIRED(45026, "Token 已过期"),
    /**
     * 不支持的 Token
     */
    TOKEN_UNSUPPORTED(45027, "不支持的 Token"),
    /**
     * Token 无效
     */
    TOKEN_INVALID(45028, "Token 无效"),
    /**
     * 无效的 Token 签名
     */
    TOKEN_SIGNATURE_INVALID(45029, "无效的 Token 签名"),
    /**
     * Token 参数不存在
     */
    TOKEN_NO_SUCH_PARAMETER(45030, "Token 参数不存在"),
    /**
     * 用户被冻结，请联系管理员
     */
    USER_INVALID(45031, "用户被冻结，请联系管理员"),
    /**
     * 菜单包含子菜单
     */
    MENU_CHILDREN_EXIST(45032, "菜单包含子菜单"),
    /**
     * 没有数据
     */
    NO_RECORDS(45033, "暂时没有数据"),
    /**
     * 博客不存在
     */
    BLOG_NOT_EXIST(45034, "博客不存在"),
    /**
     * 字典已存在
     */
    DICT_EXIST(45035, "字典已存在"),
    /**
     * 字典不存在
     */
    DICT_NOT_EXIST(45036, "字典不存在"),
    /**
     * 字典数据已存在
     */
    DICT_DATA_EXIST(45037, "字典数据已存在"),
    /**
     * 字典数据不存在
     */
    DICT_DATA_NOT_EXIST(45038, "字典数据不存在"),
    /**
     * 数据不存在
     */
    DATA_NOT_EXIST(45039, "数据不存在"),
    /**
     * 数据已存在
     */
    DATA_EXIST(45040, "数据已存在"),
    /**
     * 任务正在运行，无需再次运行
     */
    JOB_ON_RUNNING(45041, "任务正在运行，无需再次运行"),
    /**
     * 任务已经暂停，无需再次暂停
     */
    JOB_ON_PAUSING(45042, "任务已经暂停，无需再次暂停"),
    /**
     * 数据正在审核
     */
    DATA_UNDER_REVIEW(45043, "数据正在审核"),
    /**
     * 文件删除失败
     */
    FILE_DELETE_FAIL(45044, "文件删除失败"),
    /**
     * 文件操作失败
     */
    FILE_OPERATE_FAIL(45045, "文件操作失败"),
    /**
     * 获取文件日志失败
     */
    GET_FILELOG_FAIL(45046, "获取文件日志失败"),
    /**
     * 获取cdn数据失败
     */
    GTE_CDNRESULT_FAIL(45047, "获取cdn数据失败"),
    /**
     * 文件大小超出限制
     */
    FILE_SIZE_OVERFLOW(45048, "文件大小超出限制"),


    /**
     * 警告：文章未添加标签
     */
    WARNING_TAG_EMPTY(54001, "警告：文章未添加标签"),
    /**
     * 警告：这不是你的文章，不能修改
     */
    WARNING_ARTICLE_BAD_REQUEST(54002, "警告：这不是你的文章，不能修改"),

    /**
     * 异常：获取用户异常
     */
    UNAUTHORIZED_USER(54001, "获取用户异常"),
    /**
     * 异常：获取用户ID异常
     */
    UNAUTHORIZED_ID(54002, "获取用户ID异常"),
    /**
     * 异常：获取用户Account异常
     */
    UNAUTHORIZED_ACCOUNT(54003, "获取用户账号异常");

    final Integer code;
    final String msg;

    AppHttpCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
