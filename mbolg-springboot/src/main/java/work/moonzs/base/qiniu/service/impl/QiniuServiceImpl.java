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
import work.moonzs.base.utils.IFileUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.vo.sys.SysQiniuFileVO;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
     * qiniu目录标识
     */
    public static final String QINIU_DIR_SIGN = "application/qiniu-object-manager";

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
    public List<SysQiniuFileVO> listFile(String bucket) {
        List<FileInfo> result = new ArrayList<>();
        BucketManager.FileListIterator fileListIterator = qiniuManager.getFileListIterator(bucket);
        while (fileListIterator.hasNext()) {
            // 处理获取的file list结果
            FileInfo[] next = fileListIterator.next();
            if (next != null) {
                result.addAll(List.of(next));
            }
        }
        // 将结果格式化成树形结构
        List<SysQiniuFileVO> list = basicDealList(result);
        return buildFileTree(list);
    }

    /**
     * 基本处理列表
     *
     * @param fileInfoList 文件信息列表
     * @return {@link List}<{@link SysQiniuFileVO}>
     */
    private List<SysQiniuFileVO> basicDealList(List<FileInfo> fileInfoList) {
        List<SysQiniuFileVO> result = new ArrayList<>();
        fileInfoList.forEach(fileInfo -> {
            // 如果是官方认定的目录的话，就直接跳过，目录由自己生成
            if (QINIU_DIR_SIGN.equals(fileInfo.mimeType)) {
                return;
            }
            // 不是官方认定的目录但是就是目录，也跳过
            if (fileInfo.key.lastIndexOf("/") == (fileInfo.key.length() - 1)) {
                return;
            }
            SysQiniuFileVO sysQiniuFileVO = copyFileInfoBean(fileInfo);
            sysQiniuFileVO.setFileName(fileInfo.key);
            // 不是目录则可以直接添加到结果中
            result.add(sysQiniuFileVO);
        });
        return result;
    }

    /**
     * 构建文件树
     *
     * @param list    列表
     * @param baseDir 上一级目录
     * @return {@link List}<{@link SysQiniuFileVO}>
     */
    private List<SysQiniuFileVO> buildFileTree(List<SysQiniuFileVO> list, String baseDir) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 当前层级的所有文件，包括目录，以及非目录的文件
        List<SysQiniuFileVO> result = new ArrayList<>();
        // 当前层级的目录结构
        Map<String, List<SysQiniuFileVO>> dirs = new HashMap<>();
        for (SysQiniuFileVO sysQiniuFileVO : list) {
            String fileName = sysQiniuFileVO.getFileName();
            // 没有"/"说明是当前目录下的文件，直接加到结果结果集中
            int index = fileName.indexOf("/");
            if (index < 0) {
                result.add(sysQiniuFileVO);
            } else {
                // 否则判断该文件属于哪个祖先目录下的，并加入到该目录中准备递归处理
                String fileKey = fileName.substring(0, index + 1).trim();
                // 给实体重新设置一个名字，该名字去除了最外层目录
                String lastName = fileName.substring(index + 1).trim();
                if (StrUtil.isNotBlank(lastName)) {
                    sysQiniuFileVO.setFileName(lastName);
                }
                if (dirs.containsKey(fileKey)) {
                    dirs.get(fileKey).add(sysQiniuFileVO);
                } else {
                    dirs.put(fileKey, new ArrayList<>(List.of(sysQiniuFileVO)));
                }
            }
        }
        dirs.forEach((key, value) -> {
            // 以当前的key值(10/)作为目录的文件名
            SysQiniuFileVO dirSysQiniuFileVO = buildDirSysQiniuFileVO();
            // 实体的key为基本目录加集合key值(2022/+10/)
            String fileKey = StrUtil.isNotBlank(baseDir) ? baseDir + key : key;
            dirSysQiniuFileVO.setKey(fileKey);
            dirSysQiniuFileVO.setFileName(key);
            dirSysQiniuFileVO.setChildren(buildFileTree(value, fileKey));
            result.add(dirSysQiniuFileVO);
        });
        return result;
    }

    private List<SysQiniuFileVO> buildFileTree(List<SysQiniuFileVO> list) {
        return buildFileTree(list, null);
    }

    /**
     * 构建目录文件结构
     *
     * @return {@link SysQiniuFileVO}
     */
    private SysQiniuFileVO buildDirSysQiniuFileVO() {
        return SysQiniuFileVO.builder().fsize(0L).mimeType(QINIU_DIR_SIGN).type(0).status(0).isDir(true).children(new ArrayList<>()).build();
    }

    /**
     * 构建FileInfo为SysQiniuFileVO
     *
     * @param fileInfo 文件信息
     * @return {@link SysQiniuFileVO}
     */
    private SysQiniuFileVO copyFileInfoBean(FileInfo fileInfo) {
        return SysQiniuFileVO.builder().key(fileInfo.key).hash(fileInfo.hash).fsize(fileInfo.fsize).putTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileInfo.putTime)).mimeType(fileInfo.mimeType).type(fileInfo.type).status(fileInfo.status).build();
    }

    @Override
    public void deleteFile(String bucket, String[] keys) {
        // 去除目录的key
        keys = removeDirkey(keys);
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

    /**
     * 删除目录key
     *
     * @param keys 键
     * @return {@link String[]}
     */
    private String[] removeDirkey(String[] keys) {
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            if (StrUtil.isBlank(key)) {
                continue;
            }
            if (!key.contains("/") || key.lastIndexOf("/") == (key.length() - 1)) {
                result.add(key);
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    public boolean changeType(String bucket, String fileName, String newType) {
        boolean result = false;
        try {
            qiniuManager.changeMime(fileName, newType, bucket);
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
        // 对srcKey和destKey进行检查，若不符合条件则不进行操作
        boolean result = false;
        if (!checkFileKeys(srcKey, destKey)) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
        // 文件后缀不能变
        if (!checkFileType(srcKey, destKey)) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
        try {
            qiniuManager.moveOrCopyFile(srcBucket, srcKey, destBucket, destKey, fileAction);
            result = true;
        } catch (QiniuException e) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_OPERATE_FAIL);
        }
        return result;
    }

    /**
     * 检查文件类型不能改变
     *
     * @param srcKey  源文件
     * @param destKey 目标文件
     * @return boolean
     */
    private boolean checkFileType(String srcKey, String destKey) {
        int dotIndex1 = srcKey.lastIndexOf(".");
        int dotIndex2 = destKey.lastIndexOf(".");
        return srcKey.substring(dotIndex1).equals(destKey.substring(dotIndex2));
    }

    /**
     * 检查所有文件key是否符合要求
     *
     * @param keys 键
     * @return boolean
     */
    private boolean checkFileKeys(String... keys) {
        boolean result = false;
        if (keys.length <= 0) {
            return result;
        }
        for (String key : keys) {
            result = checkFileKey(key);
            if (!result) {
                break;
            }
        }
        return result;
    }

    /**
     * 检查文件key是否符合要求
     *
     * @param key 关键
     * @return boolean
     */
    private boolean checkFileKey(String key) {
        // 为空直接返回false
        if (StrUtil.isBlank(key)) {
            return false;
        }
        // 为目录直接返回false
        if (key.lastIndexOf("/") == (key.length() - 1)) {
            return false;
        }
        // 判断是否是合法的文件名
        int dotIndex = key.lastIndexOf(".");
        return IFileUtil.isLegalFileType(key.substring(dotIndex + 1));
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
