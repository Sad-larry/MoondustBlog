package work.moonzs.service;

import org.springframework.web.multipart.MultipartFile;

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
     * @return {@link String}
     */
    String uploadImage(MultipartFile image);
}
