package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 七牛云文件
 *
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysQiniuFileVO {
    /**
     * 文件全路径，也是文件名称
     */
    private String key;
    /**
     * ETag
     */
    private String hash;
    /**
     * fsize，文件大小
     */
    private long fsize;
    /**
     * 最近更新时间
     */
    private String putTime;
    /**
     * 文件类型
     */
    private String mimeType;
    /**
     * 存储类型
     */
    private int type;
    /**
     * 文件状态
     */
    private int status;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 是否为目录结构
     */
    private boolean isDir;
    /**
     * 如果是目录，则有子节点
     */
    private List<SysQiniuFileVO> children;
}
