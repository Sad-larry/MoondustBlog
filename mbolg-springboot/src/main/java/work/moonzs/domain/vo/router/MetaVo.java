package work.moonzs.domain.vo.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由显示信息
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaVo {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;
    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;
    /**
     * 设置为true，则不会被<keep-alive>缓存
     */
    private Boolean keepAlive;
}
