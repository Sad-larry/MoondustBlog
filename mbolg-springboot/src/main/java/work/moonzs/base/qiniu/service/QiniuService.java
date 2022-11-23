package work.moonzs.base.qiniu.service;

import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.qiniu.config.QiniuManager;
import work.moonzs.domain.vo.sys.SysQiniuFileVO;

import java.util.List;
import java.util.Map;

/**
 * @author Moondust月尘
 */
public interface QiniuService {
    /**
     * 上传本地文件
     * 这里的文件名一般是带有全路径的文件的文件名，通常是上传本地文件
     * 一般不用这个方法，一般在浏览器上传文件，服务器截取文件流进行存储
     * <p>
     * 接下来的所有的key，都是文件在存储空间上的完整路径，例如文件what.jpg上传时
     * 会生成一个yyyy/MM/dd/uuid.jpg的关键路径作为key，这个uuid是随机生成的字符串
     * 防止在同一文件夹中重复文件名导致上传失败
     *
     * @param bucket   存储空间
     * @param key      文件存储路径
     * @param filename 文件名
     * @return {@link Boolean}
     */
    boolean uploadFile(String bucket, String key, String filename);

    /**
     * 上传本地文件，文件流
     *
     * @param bucket 存储空间
     * @param key    文件存储路径
     * @param file   文件
     * @return {@link Boolean}
     */
    boolean uploadFile(String bucket, String key, MultipartFile file);

    /**
     * 获取空间文件列表
     *
     * @param bucket 存储空间
     * @return {@link SysQiniuFileVO}
     */
    List<SysQiniuFileVO> listFile(String bucket);

    /**
     * 删除文件
     *
     * @param bucket 存储空间
     * @param keys   存储文件关键路径
     */
    void deleteFile(String bucket, String[] keys);


    /**
     * 修改文件类型
     *
     * @param bucket  存储空间
     * @param key     文件名称
     * @param newType 新型
     * @return boolean
     */
    boolean changeType(String bucket, String key, String newType);

    /**
     * 重命名文件
     *
     * @param bucket  存储空间
     * @param oldName 旧名称
     * @param newName 新名字
     * @return boolean
     */
    boolean renameFile(String bucket, String oldName, String newName);

    /**
     * 移动或复制文件
     *
     * @param srcBucket  源存储空间
     * @param srcKey     源存储路径
     * @param destBucket 目标存储空间
     * @param destKey    目标存储路径
     * @param fileAction 文件操作(COPY、MOVE)
     * @return boolean
     */
    boolean moveOrCopyFile(String srcBucket, String srcKey, String destBucket, String destKey,
                           QiniuManager.FileAction fileAction);

    /**
     * 设置文件生存时间
     *
     * @param bucket 存储空间
     * @param key    关键路径
     * @param days   天数
     */
    void setFileLife(String bucket, String key, int days);

    /**
     * 更新镜像源
     *
     * @param bucket 存储空间
     * @param key    关键路径
     */
    void updateFile(String bucket, String key);

    /**
     * 公有下载
     *
     * @param domain 域
     * @param key    文件全路径名称
     * @return {@link String}
     */
    String publicDownload(String domain, String key);

    /**
     * 私有下载
     * 返回下载文件的网络链接，后端不提供下载资源，只提供下载资源的URI
     * 需要前端访问该链接下载文件
     *
     * @param domain 域名
     * @param key    文件全路径名称
     * @return {@link String}
     */
    String privateDownload(String domain, String key);

    /**
     * 刷新文件
     *
     * @param domain 域
     * @param keys   关键路径
     */
    void refreshFile(String domain, String[] keys);

    /**
     * 下载cdn日志
     *
     * @param domains 域
     * @param logDate 日志日期
     * @return {@link List}<{@link String}>
     */
    List<String> downloadCdnLog(String[] domains, String logDate);

    /**
     * 获取空间带宽统计，使用自定义单位
     *
     * @param domains   域
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param unit      数据单位KB,MB,GB
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getBucketBandwidth(String[] domains, String startDate, String endDate,
                                           String unit);

    /**
     * 获取空间的流量统计，使用自定义单位
     *
     * @param domains   域
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param unit      单位
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getBucketFlux(String[] domains, String startDate, String endDate, String unit);
}
