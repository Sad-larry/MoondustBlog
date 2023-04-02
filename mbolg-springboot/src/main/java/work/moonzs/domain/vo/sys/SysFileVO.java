package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 七牛云单文件
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysFileVO {
    /**
     * 用户文件id
     */
    private String id;
    /**
     * 用户文件名
     */
    private String filename;
    /**
     * 扩展名
     */
    private String extension;
    /**
     * 所在目录，根目录为null
     */
    private String pid;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 链接
     */
    private String url;
    /**
     * 文件是否是目录，用于区分文件夹与普通文件
     */
    private boolean dir;
    /**
     * 文件最后修改时间
     */
    private Date updateTime;
    /**
     * 文件创建时间
     */
    private Date createTime;
    /**
     * 文件删除时间
     */
    private Date deleteTime;
}
