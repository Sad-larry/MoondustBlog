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
 * 字典表(Dict)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_dict")
public class Dict {
    //主键ID    
    @TableId
    private Long id;
    //字典名称
    private String name;
    //字典类型
    private String type;
    //是否发布(0否,1是)
    private Integer isPublish;
    //排序
    private Integer sort;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}

