package work.moonzs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Menu)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
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
    private String url;
    //组件
    private String component;
    //菜单图标
    private String icon;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

