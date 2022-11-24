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
public class SysPhotoVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 相册ID
     */
    private Long albumId;
    /**
     * 照片名
     */
    private String name;
    /**
     * 照片描述
     */
    private String info;
    /**
     * 照片地址
     */
    private String url;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

