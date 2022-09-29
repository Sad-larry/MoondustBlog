package work.moonzs.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CategoryListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 分类名
     */
    private String categoryName;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 分类状态(1正常,0停用)
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
}
