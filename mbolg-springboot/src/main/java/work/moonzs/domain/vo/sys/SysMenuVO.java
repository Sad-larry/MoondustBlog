package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.domain.entity.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuVO {
    /**
     * id
     */
    private Long id;
    /**
     * 上级资源ID
     */
    private Long parentId;
    /**
     * 资源名称
     */
    private String title;
    /**
     * 跳转地址
     */
    private String name;
    /**
     * 路由地址
     */
    private String url;
    /**
     * 资源组件
     */
    private String component;
    /**
     * 资源图标
     */
    private String icon;
    /**
     * 资源级别
     */
    private Integer level;
    /**
     * 显示顺序
     */
    private Integer sortNo;
    /**
     * 菜单类型(M菜单,F按钮)
     */
    private String type;
    /**
     * 重定向地址
     */
    private String redirect;
    /**
     * 是否隐藏(0否,1是)
     */
    private Integer hidden;
    /**
     * 是否缓存（0不缓存,1缓存）
     */
    private Integer isCache;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 子菜单
     */
    List<Menu> children = new ArrayList<>();
}
