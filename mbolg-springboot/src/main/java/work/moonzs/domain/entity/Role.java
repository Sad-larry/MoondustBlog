package work.moonzs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Role)表实体类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role")
public class Role {
    //主键    
    @TableId
    private Long id;
    //角色名
    private String roleName;
    //角色描述
    private String description;
    //角色状态(1正常,0停用)
    private String status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

