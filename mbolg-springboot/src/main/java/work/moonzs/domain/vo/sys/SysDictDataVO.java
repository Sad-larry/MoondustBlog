package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.moonzs.domain.entity.Dict;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictDataVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 字典类型ID
     */
    private Long dictId;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典键值
     */
    private String value;
    /**
     * 回显样式
     */
    private String style;
    /**
     * 是否默认(0否,1是)
     */
    private Integer isDefault;
    /**
     * 是否发布(0否,1是)
     */
    private Integer isPublish;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 字典数据
     */
    private Dict dict;
}
