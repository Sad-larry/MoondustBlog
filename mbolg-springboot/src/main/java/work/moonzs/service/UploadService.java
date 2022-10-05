package work.moonzs.service;

import org.springframework.web.multipart.MultipartFile;
import work.moonzs.domain.ResponseResult;

/**
 * 文件上传接口
 *
 * @author Moondust月尘
 */
public interface UploadService {

    /**
     * 上传图片
     *
     * @param image 图像
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> uploadImage(MultipartFile image);
}
