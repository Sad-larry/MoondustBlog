package work.moonzs.domain.vo.sys;

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
public class SysPermissionVO {
    /**
     * id
     */
    private Long id;
    /**
     * 上级资源ID
     */
    private Long parentId;
    /**
     * 资源名称
     */
    private String title;
    /**
     * 路由地址
     */
    private String url;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 子权限
     */
    private List<SysPermissionVO> children;
}
