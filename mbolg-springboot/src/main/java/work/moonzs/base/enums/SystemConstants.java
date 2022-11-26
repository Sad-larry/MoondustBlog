package work.moonzs.base.enums;

/**
 * 服务器常量
 *
 * @author Moondust月尘
 */
public class SystemConstants {
    /**
     * 一般状态-失败状态
     */
    public static final String FAIL = "fail";
    /**
     * 一般状态-成功状态
     */
    public static final String SUCCESS = "success";
    /**
     * 登录用户关键，在token创建过程中需要，解析时需要
     */
    public static final String LOGIN_USER_KEY = "login_user_uid";
    /**
     * 登录用户令牌
     */
    public static final String TOKEN = "token";
    /**
     * 页面num
     */
    public static final Integer PAGE_NUM = 1;
    /**
     * 页面大小
     */
    public static final Integer PAGE_SIZE = 10;
    /**
     * 文件大小限制
     */
    public static final long FILE_SIZE = 5 * 1024 * 1024L;
}
