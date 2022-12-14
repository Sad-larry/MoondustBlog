package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {
    /**
     * id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * url
     */
    private String path;
    /**
     * 组件
     */
    private String component;
    /**
     * 图标
     */
    private String icon;
    /**
     * 子菜单列表
     */
    private List<MenuTreeVo> children = new ArrayList<>();
}
