package work.moonzs.domain.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {
    /**
     * id
     */
    private Long id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 文章数
     */
    private Integer articleNum;
}
