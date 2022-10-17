package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Role)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-17 14:28:15
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
    //角色状态(0停用,1正常)
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

