package work.moonzs.domain.vo.sys;

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
public class SysPhotoAlbumVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 相册名
     */
    private String name;
    /**
     * 相册描述
     */
    private String info;
    /**
     * 相册封面
     */
    private String cover;
    /**
     * 相册状态(0私密,1公开)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 照片数量
     */
    private Long photoCount;
}

