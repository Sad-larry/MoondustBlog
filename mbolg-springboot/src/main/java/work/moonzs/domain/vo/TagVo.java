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
public class TagVo {
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
     * 标签状态(0停用,1正常)
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
}