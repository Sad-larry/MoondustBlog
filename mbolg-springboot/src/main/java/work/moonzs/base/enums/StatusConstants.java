package work.moonzs.base.enums;

/**
 * 数据库中的状态常量
 *
 * @author Moondust月尘
 */
public class StatusConstants {
    /**
     * 停用状态为0
     */
    public static final Integer DISABLE = 0;
    /**
     * 正常状态为1
     */
    public static final Integer NORMAL = 1;
    /**
     * 删除状态为2
     */
    public static final Integer DELETE = 2;
    /**
     * 菜单类型-目录
     */
    public static final String TYPE_DIR = "M";
    /**
     * 菜单类型-菜单
     */
    public static final String TYPE_MENU = "C";
    /**
     * 菜单类型-按钮
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * 反馈-需求
     */
    public static final Integer FEEDBACK_DEMAND = 1;
    /**
     * 反馈-缺陷
     */
    public static final Integer FEEDBACK_DEFECTS = 2;
    /**
     * SQL语句限制 限制 1 条数据
     */
    public static final String LIMIT_ONE = "LIMIT 1";
    /**
     * SQL语句限制 限制 5 条数据
     */
    public static final String LIMIT_FIVE = "LIMIT 5";

    /**
     * 上一篇文章
     */
    public static final Integer LAST_ARTICLE = 0;
    /**
     * 下一篇文章
     */
    public static final Integer NEXT_ARTICLE = 1;
}
