package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    /**
     * id
     */
    private Long id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类点击量
     */
    private Integer clickVolume;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
}
