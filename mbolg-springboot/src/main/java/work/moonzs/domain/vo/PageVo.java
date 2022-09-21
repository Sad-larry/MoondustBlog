package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {
    /**
     * 分页查询查询出来的数据
     */
    private List<T> datas;
    /**
     * 分页数据条数
     */
    private Long total;
    /**
     * 当前页码数
     */
    private Long current;
    /**
     * 每页显示条数
     */
    private Long size;
}
