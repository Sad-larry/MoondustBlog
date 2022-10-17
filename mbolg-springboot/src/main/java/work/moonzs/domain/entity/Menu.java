package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Menu)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:31:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {
    //主键    
    @TableId
    private Long id;
    //父级菜单id
    private Long pid;
    //组件
    private String component;
    //菜单图标
    private String icon;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //菜单状态(0停用,1正常)
    private Integer status;
    //菜单名
    private String name;
    //路由地址
    private String path;
}

