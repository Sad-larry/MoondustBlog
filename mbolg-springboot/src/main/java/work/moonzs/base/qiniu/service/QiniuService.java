package work.moonzs.base.qiniu.service;

import com.qiniu.http.Response;
import com.qiniu.storage.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Moondust月尘
 */
public interface QiniuService {
    /**
     * 上传本地文件
     * 这里的文件名一般是带有全路径的文件的文件名，通常是上传本地文件
     * 一般不用这个方法，一般在浏览器上传文件，服务器截取文件流进行存储
     *
     * @param bucket   存储空间
     * @param key      文件存储路径
     * @param filename 文件名
     * @return {@link Response}
     */
    Response uploadFile(String bucket, String key, String filename);

    /**
     * 上传本地文件，文件流
     *
     * @param bucket 存储空间
     * @param key    文件存储路径
     * @param file   文件
     * @return {@link Response}
     */
    Response uploadFile(String bucket, String key, MultipartFile file);

    /**
     * 获取空间文件列表
     *
     * @return {@link FileInfo[]}
     */
    FileInfo[] listFile();

    /**
     * 删除文件
     *
     * @return {@link Response}
     */
    Response deleteFile(String bucket, String key);
}
