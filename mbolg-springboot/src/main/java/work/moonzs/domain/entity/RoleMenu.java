package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色-权限资源关联表(RoleMenu)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_menu")
public class RoleMenu {
    //主键ID    
    @TableId
    private Long id;
    //角色ID
    private Long roleId;
    //菜单ID
    private Long menuId;
}

