package work.moonzs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 角色表(Role)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role")
public class Role {
    //主键ID
    @TableId
    private Long id;
    //角色编码
    private String code;
    //角色名称
    private String name;
    //角色描述
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

