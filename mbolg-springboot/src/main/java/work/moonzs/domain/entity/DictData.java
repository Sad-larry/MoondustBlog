package work.moonzs.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典数据表(DictData)表实体类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_dict_data")
public class DictData {
    //主键ID    
    @TableId
    private Long id;
    //字典类型ID
    private Long dictId;
    //字典标签
    private String label;
    //字典键值
    private String value;
    //回显样式
    private String style;
    //是否默认(0否,1是)
    private Integer isDefault;
    //是否发布(0否,1是)
    private Integer isPublish;
    //排序
    private Integer sort;
    //备注
    private String remark;
}

