package work.moonzs.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private String status;
    /**
     * 菜单id
     */
    private List<Long> menuIds;
    /**
     * 资源id
     */
    private List<Long> resourceIds;
    /**
     * 创建时间
     */
    private Date createTime;
}
