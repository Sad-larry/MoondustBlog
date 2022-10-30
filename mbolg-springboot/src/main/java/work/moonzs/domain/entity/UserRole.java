package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色关联表(UserRole)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_role")
public class UserRole {
    //主键ID    
    @TableId
    private Long id;
    //用户ID
    private Long userId;
    //角色ID
    private Long roleId;
}

