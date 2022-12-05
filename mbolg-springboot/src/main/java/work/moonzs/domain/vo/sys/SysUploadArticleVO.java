package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUploadArticleVO {
    /**
     * 需要上传的图片路径
     */
    private Set<String> imageUrl;
    /**
     * 存储在缓存中的图片 key
     */
    private String imageCacheKey;
    /**
     * md文件内容
     */
    private String contentMd;
}
