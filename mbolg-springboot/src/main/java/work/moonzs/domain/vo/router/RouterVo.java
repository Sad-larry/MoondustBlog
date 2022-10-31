package work.moonzs.domain.vo.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由配置信息
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterVo {
    /**
     * 路由名字
     */
    private String name;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;
    /**
     * 组件地址
     */
    private String component;
    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     * 如果不设置这个，当子路由为1个时，子路由就会变成菜单项
     */
    private Boolean alwaysShow;
    /**
     * 是否隐藏
     */
    private Boolean hidden;
    /**
     * 显示顺序
     */
    private Integer sortNo;
    /**
     * 其他元素
     */
    private MetaVo meta;
    /**
     * 子路由
     */
    private List<RouterVo> children = new ArrayList<>();
}
