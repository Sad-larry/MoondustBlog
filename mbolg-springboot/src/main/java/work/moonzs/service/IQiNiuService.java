package work.moonzs.service;

import com.qiniu.http.Response;
import com.qiniu.storage.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 七牛云存储空间的操作接口
 *
 * @author Moondust月尘
 */
public interface IQiNiuService {
    /**
     * 七牛云上传文件
     *
     * @param file    文件
     * @param fileKey 文件关键
     * @return 七牛上传Response
     * @throws IOException ioe
     */
    Response uploadFile(MultipartFile file, String fileKey) throws IOException;

    /**
     * 上传图片并返回是否成功上传
     *
     * @param image 图像
     * @return boolean
     */
    boolean isUploadImage(MultipartFile image);

    /**
     * 上传图片并返回上传后的图片名
     *
     * @param image 图像
     * @return {@link String}
     */
    String uploadImage(MultipartFile image);

    /**
     * 获取文件信息
     *
     * @param fileKey 文件关键字
     * @return {@link FileInfo}
     */
    FileInfo getFileInfo(String fileKey);

    /**
     * 更新文件类型
     * 文件类型应该是
     *
     * @param fileKey     文件关键
     * @param newMimeType 新mime类型
     */
    void updateFileType(String fileKey, String newMimeType);

    /**
     * 设置或更新文件的生存时间
     *
     * @param fileKey 文件关键
     * @param dayTime 一天时间
     */
    void setLifeTime(String fileKey, Integer dayTime);

    /**
     * 文件列表信息
     *
     * @return {@link FileInfo[]}
     */
    FileInfo[] listFileInfo();

    /**
     * 文件列表信息
     *
     * @param prefix 文件名前缀
     * @return {@link FileInfo[]}
     */
    FileInfo[] listFileInfo(String prefix);

    /**
     * 文件列表信息
     *
     * @param prefix    文件名前缀
     * @param delimiter 目录分隔符
     * @return {@link FileInfo[]}
     */
    FileInfo[] listFileInfo(String prefix, String delimiter);

}
