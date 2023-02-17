package work.moonzs.controller.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.qiniu.config.QiniuManager;
import work.moonzs.base.qiniu.service.QiniuService;
import work.moonzs.base.utils.IFileUtil;
import work.moonzs.base.utils.PathUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.ResponseResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moondust月尘
 */
@RestController("SystemQiniuC")
@RequestMapping("/system/qiniu")
@RequiredArgsConstructor
public class QiniuController {
    /**
     * 七牛云的存储空间，一般我都是存在一个空间里面，如果多个存储空间，就需要手动传参
     */
    @Value("${oss.qiniu.bucket}")
    private String BUCKET;
    @Value("${oss.qiniu.domain}")
    private String DOMAIN;

    private final QiniuService qiniuService;
    private final RedisCache redisCache;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file) {
        IFileUtil.isLegalFile(file);
        String filePath = PathUtil.generateFilePath(file.getOriginalFilename());
        boolean b = qiniuService.uploadFile(BUCKET, filePath, file);
        return ResponseResult.success(qiniuService.publicDownload(DOMAIN, filePath));
    }

    /**
     * 上传文章为渲染的图片
     *
     * @param file 文件
     * @param key  关键
     * @return {@link ResponseResult}
     */
    @PostMapping(path = "/upload/article/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult uploadArticleImage(MultipartFile file, @RequestParam("key") String key) {
        if (file == null) {
            return ResponseResult.fail(500, "文件不能为空");
        }
        // 判断文件类型
        if (!IFileUtil.determiningFile(file)) {
            return ResponseResult.fail(500, "文件类型错误");
        }
        String filename = file.getOriginalFilename();
        boolean hasKey = redisCache.hHasKey(CacheConstants.NEED_UPLOAD_IMAGE + key, filename);
        if (hasKey) {
            String filePath = (String) redisCache.hget(CacheConstants.NEED_UPLOAD_IMAGE + key, filename);
            qiniuService.uploadFile(BUCKET, filePath, file);
            redisCache.hdel(CacheConstants.NEED_UPLOAD_IMAGE + key, filename);
            Map<String, String> result = new HashMap<>();
            result.put("filename", filename);
            result.put("fileLink", qiniuService.publicDownload(DOMAIN, filePath));
            return ResponseResult.success(result);
        }
        return ResponseResult.fail(AppHttpCodeEnum.FILE_UPLOAD_FAIL);
    }

    /**
     * 列出存储空间的文件
     *
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "列出存储空间的文件")
    @GetMapping("/file/list")
    public ResponseResult listFile() {
        return ResponseResult.success(qiniuService.listFile(BUCKET));
    }

    /**
     * 删除文件
     *
     * @param bucket 存储空间
     * @param keys   键
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "删除存储空间的文件")
    @PostMapping("/file/delete")
    public ResponseResult deleteFile(@RequestParam(value = "bucket", required = false) String bucket, @RequestBody String[] keys) {
        qiniuService.deleteFile(BUCKET, keys);
        return ResponseResult.success();
    }

    /**
     * 修改存储空间的文件类型
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "修改存储空间的文件类型")
    @PostMapping("/file/changeType")
    public ResponseResult changeType(@RequestBody Map<String, Object> map) {
        String fileName = JSONUtil.toJsonStr(map.get("fileName"));
        String newType = JSONUtil.toJsonStr(map.get("newType"));
        if (StrUtil.isAllNotBlank(fileName, newType)) {
            qiniuService.changeType(BUCKET, fileName, newType);
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    /**
     * 重命名文件
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "重命名文件")
    @PostMapping("/file/rename")
    public ResponseResult renameFile(@RequestBody Map<String, Object> map) {
        String oldName = JSONUtil.toJsonStr(map.get("oldName"));
        String newName = JSONUtil.toJsonStr(map.get("newName"));
        if (StrUtil.isAllNotBlank(oldName, newName)) {
            qiniuService.renameFile(BUCKET, oldName, newName);
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    /**
     * 移动或复制文件
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "移动或复制文件")
    @PostMapping("/file/moveOrcopy")
    public ResponseResult moveOrCopyFile(@RequestBody Map<String, Object> map) {
        String srcKey = JSONUtil.toJsonStr(map.get("srcKey"));
        String destKey = JSONUtil.toJsonStr(map.get("destKey"));
        String fileAction = JSONUtil.toJsonStr(map.get("fileAction"));
        if (StrUtil.isAllNotBlank(srcKey, destKey, fileAction)) {
            if (QiniuManager.FileAction.MOVE.name().equalsIgnoreCase(fileAction)) {
                qiniuService.moveOrCopyFile(BUCKET, srcKey, BUCKET, destKey, QiniuManager.FileAction.MOVE);
            } else if (QiniuManager.FileAction.COPY.name().equalsIgnoreCase(fileAction)) {
                qiniuService.moveOrCopyFile(BUCKET, srcKey, BUCKET, destKey, QiniuManager.FileAction.COPY);
            } else {
                return ResponseResult.fail();
            }
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    /**
     * 设置文件生存时间
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "设置文件生存时间")
    @PostMapping("/file/setLife")
    public ResponseResult setFileLife(@RequestBody Map<String, Object> map) {
        String key = JSONUtil.toJsonStr(map.get("key"));
        String days = JSONUtil.toJsonStr(map.get("days"));
        if (StrUtil.isAllNotBlank(key, days)) {
            qiniuService.setFileLife(BUCKET, key, Integer.parseInt(days));
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    /**
     * 更新镜像源
     *
     * @param key 关键
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新镜像源")
    @GetMapping("/file/update")
    public ResponseResult updateFile(@RequestParam("key") String key) {
        if (true) {
            return ResponseResult.fail(404, "TODO 开发中");
        }
        qiniuService.updateFile(BUCKET, key);
        return ResponseResult.success();
    }

    /**
     * 公有下载
     *
     * @param key 关键
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "公有下载")
    @GetMapping("/file/download")
    public ResponseResult publicDownload(@RequestParam("key") String key) {
        return ResponseResult.success(qiniuService.publicDownload(DOMAIN, key));
    }

    /**
     * 私有下载
     *
     * @param key 关键
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "私有下载")
    @GetMapping("/file/pridownload")
    public ResponseResult privateDownload(@RequestParam("key") String key) {
        return ResponseResult.success(qiniuService.privateDownload(DOMAIN, key));
    }

    /**
     * 刷新文件
     *
     * @param keys 键
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "刷新文件")
    @PostMapping("/file/refresh")
    public ResponseResult refreshFile(@RequestBody String[] keys) {
        qiniuService.refreshFile(DOMAIN, keys);
        return ResponseResult.success();
    }


    /**
     * 下载cdn日志
     *
     * @param logDate 日志日期
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "下载cdn日志")
    @GetMapping("/file/log/download")
    public ResponseResult downloadCdnLog(@RequestParam("logDate") String logDate) {
        return ResponseResult.success(qiniuService.downloadCdnLog(new String[]{DOMAIN}, logDate));
    }


    /**
     * 获取空间带宽统计
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取空间带宽统计")
    @PostMapping("/file/bucketBandwidth")
    public ResponseResult getBucketBandwidth(@RequestBody Map<String, Object> map) {
        if (true) {
            return ResponseResult.fail(404, "TODO 开发中");
        }
        String startDate = JSONUtil.toJsonStr(map.get("startDate"));
        String endDate = JSONUtil.toJsonStr(map.get("endDate"));
        String unit = JSONUtil.toJsonStr(map.get("unit"));
        if (StrUtil.isAllNotBlank(startDate, endDate)) {
            return ResponseResult.success(qiniuService.getBucketBandwidth(new String[]{DOMAIN}, startDate, endDate, unit));
        }
        return ResponseResult.fail();
    }


    /**
     * 获取空间的流量统计
     *
     * @param map 集合
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取空间的流量统计")
    @PostMapping("/file/bucketFlux")
    public ResponseResult getBucketFlux(@RequestBody Map<String, Object> map) {
        if (true) {
            return ResponseResult.fail(404, "TODO 开发中");
        }
        String startDate = JSONUtil.toJsonStr(map.get("startDate"));
        String endDate = JSONUtil.toJsonStr(map.get("endDate"));
        String unit = JSONUtil.toJsonStr(map.get("unit"));
        if (StrUtil.isAllNotBlank(startDate, endDate)) {
            return ResponseResult.success(qiniuService.getBucketFlux(new String[]{DOMAIN}, startDate, endDate, unit));
        }
        return ResponseResult.fail();
    }
}
