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
public class CategoryVo {
    /**
     * id
     */
    private Long id;
    /**
     * 分类名称
     */
    private String categoryName;
}

