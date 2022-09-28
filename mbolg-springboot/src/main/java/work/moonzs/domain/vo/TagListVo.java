package work.moonzs.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class TagListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 标签描述
     */
    private String description;
    /**
     * 标签状态(1正常,0停用)
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}