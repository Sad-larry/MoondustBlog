package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (RoleResource)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_resource")
public class RoleResource {
    //主键    
    @TableId
    private Long id;
    //角色id
    private Long roleId;
    //权限id
    private Long resourceId;
}

