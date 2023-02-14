package work.moonzs.base.enums;

/**
 * redis键值常量
 *
 * @author Moondust月尘
 */
public class CacheConstants {
    /**
     * 存放用户token的键
     */
    public static final String TOKEN_KEY = "mblog:token:";
    /**
     * 存放唯一验证码的键
     */
    public static final String CAPTCHA_CODE_KEY = "mblog:captcha:";
    /**
     * 存放登录的用户信息
     */
    public static final String LOGIN_USER_KEY = "mblog:login:";
    /**
     * 存放系统配置的键名前缀
     */
    public static final String SYS_CONFIG_KEY = "mblog:config:";
    /**
     * 存放需要上传图片的键名前缀
     */
    public static final String NEED_UPLOAD_IMAGE = "mblog:upload:";
    /**
     * 博客浏览量
     */
    public static final String BLOG_VIEWS_QUANTITY = "mblog:viewsQuantity";
    /**
     * 博客点赞数
     */
    public static final String BLOG_LIKE_QUANTITY = "mblog:like:";
    /**
     * 存放在线用户有序集合的键
     */
    public static final String ONLINE_USER_KEY = "mblog:onlineUser";
    /**
     * 保存的登录用户键
     */
    public static final String SAVE_LOGIN_USER_KEY = "mblog:loginUser";
    /**
     * 博客访问量
     */
    public static final String BLOG_VISITS_COUNT = "mblog:visitorCount";
    /**
     * 唯一标志的访客
     */
    public static final String UNIQUE_VISITOR = "mblog:uniqueVisitor";
    /**
     * 访客地理位置
     */
    public static final String VISITOR_AREA = "mblog:visitorArea";
}
