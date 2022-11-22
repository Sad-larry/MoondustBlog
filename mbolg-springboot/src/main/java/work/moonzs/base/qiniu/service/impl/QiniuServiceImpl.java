package work.moonzs.base.qiniu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.qiniu.config.QiniuConfig;
import work.moonzs.base.qiniu.config.QiniuManager;
import work.moonzs.base.qiniu.service.QiniuService;
import work.moonzs.base.web.common.BusinessAssert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moondust月尘
 */
@Slf4j
@Service("qiniuService")
@RequiredArgsConstructor
public class QiniuServiceImpl implements QiniuService {
    private final QiniuConfig qiniuConfig;
    private final QiniuManager qiniuManager;

    /**
     * 移动文件
     *
     * @param srcBucket  源存储空间
     * @param fromKey    源存储路径
     * @param destBucket 目标存储空间
     * @param toKey      目标存储路径
     */
    private boolean moveFile(String srcBucket, String fromKey, String destBucket, String toKey) {
        return moveOrCopyFile(srcBucket, fromKey, destBucket, toKey, QiniuManager.FileAction.MOVE);
    }

    @Override
    public boolean uploadFile(String bucket, String key, String filename) {
        boolean result = false;
        String upToken = qiniuConfig.auth().uploadToken(bucket, filename);
        Response response;
        try {
            response = qiniuConfig.uploadManager().put(key, filename, upToken);
            if (response.isOK()) {
                result = true;
            }
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_UPLOAD_FAIL);
        }
        return result;
    }

    @Override
    public boolean uploadFile(String bucket, String key, MultipartFile file) {
        boolean result = false;
        String upToken = qiniuConfig.auth().uploadToken(bucket);
        Response response;
        try {
            response = qiniuConfig.uploadManager().put(file.getInputStream(), key, upToken, null, null);
            if (response.isOK()) {
                result = true;
            }
        } catch (IOException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_UPLOAD_FAIL);
        }
        return result;
    }

    @Override
    public List<FileInfo> listFile(String bucket) {
        List<FileInfo> result = new ArrayList<>();
        BucketManager.FileListIterator fileListIterator = qiniuManager.getFileListIterator(bucket);
        while (fileListIterator.hasNext()) {
            // 处理获取的file list结果
            FileInfo[] next = fileListIterator.next();
            if (next != null) {
                result.addAll(List.of(next));
            }
        }
        return result;
    }

    @Override
    public void deleteFile(String bucket, String[] keys) {
        if (ArrayUtil.isEmpty(keys)) {
            return;
        }
        try {
            BatchStatus[] batchStatusList = qiniuManager.batchDelete(bucket, keys);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code != 200) {
                    log.error("文件 {} 删除失败，错误信息: {}", keys[i], status.data.error);
                }
            }
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_DELETE_FAIL);
        }
    }

    @Override
    public boolean changeType(String bucket, String fileName, String newType) {
        boolean result = false;
        try {
            qiniuManager.changeMime(bucket, fileName, newType);
            result = true;
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
        return result;
    }

    @Override
    public boolean renameFile(String bucket, String oldName, String newName) {
        return moveFile(bucket, oldName, bucket, newName);
    }

    @Override
    public boolean moveOrCopyFile(String srcBucket, String srcKey, String destBucket, String destKey, QiniuManager.FileAction fileAction) {
        boolean result = false;
        try {
            qiniuManager.moveOrCopyFile(srcBucket, srcKey, destBucket, destKey, fileAction);
            result = true;
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
        return result;
    }

    @Override
    public void setFileLife(String bucket, String key, int days) {
        try {
            qiniuManager.deleteAfterDays(bucket, key, days);
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
    }

    @Override
    public void updateFile(String bucket, String key) {
        try {
            qiniuManager.prefetch(bucket, key);
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
    }

    @Override
    public String publicDownload(String domain, String key) {
        return StrUtil.format("https://{}/{}", domain, key);
    }

    @Override
    public String privateDownload(String domain, String key) {
        return qiniuManager.getPrivateUrl(publicDownload(domain, key));
    }

    @Override
    public void refreshFile(String domain, String[] keys) {
        if (ArrayUtil.isEmpty(keys)) {
            return;
        }
        String[] files = new String[keys.length];
        int i = 0;
        // 获取公有链接
        for (String key : keys) {
            files[i++] = publicDownload(domain, key);
        }
        try {
            qiniuManager.refreshFile(files);
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
    }

    @Override
    public List<String> downloadCdnLog(String[] domains, String logDate) {
        List<String> result = null;
        try {
            Map<String, CdnResult.LogData[]> cdnLog = qiniuManager.listCdnLog(domains, logDate);
            if (CollUtil.isNotEmpty(cdnLog)) {
                result = new ArrayList<>();
                // 返回日志链接
                for (Map.Entry<String, CdnResult.LogData[]> logs : cdnLog.entrySet()) {
                    for (CdnResult.LogData log : logs.getValue()) {
                        result.add(log.url);
                    }
                }
            }
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.GET_FILELOG_FAIL);
        }
        return result;
    }

    @Override
    public Map<String, Object> getBucketBandwidth(String[] domains, String startDate, String endDate, String unit) {
        // 获取带宽数据
        CdnResult.BandwidthResult bandwidthResult;
        try {
            bandwidthResult = qiniuManager.getBandwidthData(domains, startDate, endDate);
            if (bandwidthResult.code == 200) {
                Map<String, Object> result = new HashMap<>();
                result.put("time", bandwidthResult.time);
                Map<String, Object> data = new HashMap<>();
                bandwidthResult.data.forEach((key, value) -> {
                    data.put(key + ":china", value.china);
                    data.put(key + ":oversea", value.oversea);
                });
                result.put("data", data);
                return result;
            }
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.GTE_CDNRESULT_FAIL);
        }
        return null;
    }

    @Override
    public Map<String, Object> getBucketFlux(String[] domains, String startDate, String endDate, String unit) {
        // 获取流量数据
        CdnResult.FluxResult fluxResult = null;
        try {
            fluxResult = qiniuManager.getFluxData(domains, startDate, endDate);
            if (fluxResult.code == 200) {
                Map<String, Object> result = new HashMap<>();
                result.put("time", fluxResult.time);
                Map<String, Object> data = new HashMap<>();
                fluxResult.data.forEach((key, value) -> {
                    data.put(key + ":china", value.china);
                    data.put(key + ":oversea", value.oversea);
                });
                result.put("data", data);
                return result;
            }
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.GTE_CDNRESULT_FAIL);
        }
        return null;
    }
}
