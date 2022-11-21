package work.moonzs.base.qiniu.config;

import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.BatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 七牛云的配置管理
 *
 * @author Moondust月尘
 */
@Configuration
@RequiredArgsConstructor
public class QiniuManager {
    private final QiniuConfig qiniuConfig;
    /**
     * 自定义私有链接过期时间
     */
    private static final Long EXPIRE_IN_SECONDS = 24 * 60 * 60L;
    /**
     * 文件列表大小
     */
    private static final int LIST_SIZE = 1000;
    /**
     * 间隔时间
     */
    private static final String GRANULARITY_DAY = "day";

    public enum FileAction {
        // 复制或移动文件
        COPY, MOVE
    }

    /**
     * 获得带宽数据
     */
    public CdnResult.BandwidthResult getBandwidthData(String[] domains, String startDate, String endDate) throws QiniuException {
        return qiniuConfig.cdnManager().getBandwidthData(domains, startDate, endDate, GRANULARITY_DAY);
    }

    /**
     * 获取空间的流量统计
     */
    public CdnResult.FluxResult getFluxData(String[] domains, String startDate, String endDate) throws QiniuException {
        return qiniuConfig.cdnManager().getFluxData(domains, startDate, endDate, GRANULARITY_DAY);
    }

    /**
     * 日志下载，CDN 相关
     */
    public Map<String, CdnResult.LogData[]> listCdnLog(String[] domains, String logDate) throws QiniuException {
        return qiniuConfig.cdnManager().getCdnLogList(domains, logDate).data;
    }

    /**
     * 刷新文件，CDN 相关
     */
    public void refreshFile(String[] files) throws QiniuException {
        // 单次方法调用刷新的链接不可以超过100个
        qiniuConfig.cdnManager().refreshUrls(files);
    }

    /**
     * 私有下载
     */
    public String getPrivateUrl(String publicUrl) {
        return qiniuConfig.auth().privateDownloadUrl(publicUrl, EXPIRE_IN_SECONDS);
    }

    /**
     * 更新镜像源
     */
    public void prefetch(String bucket, String key) throws QiniuException {
        qiniuConfig.bucketManager().prefetch(bucket, key);
    }

    /**
     * 设置文件生存时间
     */
    public void deleteAfterDays(String bucket, String key, int days) throws QiniuException {
        qiniuConfig.bucketManager().deleteAfterDays(bucket, key, days);
    }

    /**
     * 重命名文件
     */
    public void renameFile(String bucket, String oldName, String newName) throws QiniuException {
        moveFile(bucket, oldName, bucket, newName);
    }

    /**
     * 移动文件
     */
    public void moveFile(String srcBucket, String fromKey, String destBucket, String toKey) throws QiniuException {
        moveOrCopyFile(srcBucket, fromKey, destBucket, toKey, FileAction.MOVE);
    }

    /**
     * 移动或复制文件
     */
    public void moveOrCopyFile(String srcBucket, String srcKey, String destBucket, String destKey,
                               FileAction fileAction) throws QiniuException {
        if (FileAction.COPY == fileAction) {
            qiniuConfig.bucketManager().copy(srcBucket, srcKey, destBucket, destKey, true);
        } else {
            qiniuConfig.bucketManager().move(srcBucket, srcKey, destBucket, destKey, true);
        }
    }

    /**
     * 修改文件类型
     */
    public void changeMime(String fileName, String newType, String bucket) throws QiniuException {
        qiniuConfig.bucketManager().changeMime(bucket, fileName, newType);
    }

    /**
     * 批量删除文件，单次批量请求的文件数量不得超过1000
     */
    public BatchStatus[] batchDelete(String bucket, String[] keys) throws QiniuException {
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucket, keys);
        Response response = qiniuConfig.bucketManager().batch(batchOperations);
        return response.jsonToObject(BatchStatus[].class);
    }

    /**
     * 获取空间文件列表
     */
    public BucketManager.FileListIterator getFileListIterator(String bucket) {
        return qiniuConfig.bucketManager().createFileListIterator(bucket, "", LIST_SIZE, "");
    }
}
