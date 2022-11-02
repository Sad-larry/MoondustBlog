package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限资源表 (Menu)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {
    //主键ID    
    @TableId
    private Long id;
    //上级资源ID
    private Long parentId;
    //资源名称
    private String title;
    //路由地址
    private String url;
    //资源组件
    private String component;
    //资源级别
    private Integer level;
    //显示顺序
    private Integer sortNo;
    //资源图标
    private String icon;
    //菜单类型(M菜单,F按钮)
    private String type;
    //重定向地址
    private String redirect;
    //跳转地址
    private String name;
    //是否隐藏(0否,1是)
    private Integer hidden;
    //是否缓存（0不缓存,1缓存）
    private Integer isCache;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    //子菜单列表
    @TableField(exist = false)
    List<Menu> children = new ArrayList<>();
}

