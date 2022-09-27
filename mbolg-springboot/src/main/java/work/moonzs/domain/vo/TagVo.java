package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}