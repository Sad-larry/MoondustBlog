package work.moonzs.domain.entity;

import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Menu)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {
    //主键    
    @TableId
    private Long id;
    //菜单名
    private String menuName;
    //父级菜单id
    private Long pid;
    //路由地址
    private String path;
    //组件
    private String component;
    //菜单图标
    private String icon;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    //菜单状态(1正常,0停用)
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return menuName.equals(menu.menuName) && path.equals(menu.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuName, path);
    }
}

