package work.moonzs.base.qiniu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.qiniu.service.IQiNiuService;
import work.moonzs.base.utils.PathUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Moondust月尘
 */
@Service("iQiniuService")
public class IQiNiuServiceImpl implements IQiNiuService, InitializingBean {
    private final UploadManager uploadManager;
    private final Auth auth;
    @Value("${oss.qiniu.bucket}")
    private String BUCKET;
    private StringMap putPolicy;

    @Autowired
    public IQiNiuServiceImpl(UploadManager uploadManager, Auth auth) {
        this.uploadManager = uploadManager;
        this.auth = auth;
    }

    @Value("${oss.qiniu.domain}")
    private String DOMAIN;
    @Value("${oss.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${oss.qiniu.secret-key}")
    private String SECRET_KEY;

    private static volatile BucketManager bucketManager;

    /**
     * 获取BucketManager静态实例
     *
     * @return {@link BucketManager}
     */
    private BucketManager getBucketManager() {
        if (bucketManager == null) {
            synchronized (this) {
                if (bucketManager == null) {
                    //构造一个带指定 Region 对象的配置类
                    Configuration cfg = new Configuration(Region.huadongZheJiang2());
                    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
                    bucketManager = new BucketManager(auth, cfg);
                }
            }
        }
        return bucketManager;
    }

    @Override
    public Response uploadFile(MultipartFile file, String fileKey) throws IOException {
        Response response = this.uploadManager.put(file.getInputStream(), fileKey, getUploadToken(), null, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file.getInputStream(), fileKey, getUploadToken(), null, null);
            retry++;
        }
        return response;
    }

    @Override
    public boolean isUploadImage(MultipartFile image) {
        return SystemConstants.FAIL.equalsIgnoreCase(uploadImage(image));
    }

    @Override
    public String uploadImage(MultipartFile image) {
        // 获取初始文件名
        String originalFilename = image.getOriginalFilename();
        // 以当前日期组成/2022/10/22/uuid格式路径
        String filePath = PathUtil.generateFilePath(originalFilename);
        // 如果判断通过上传到OSS
        String url = uploadOss(image, filePath);
        if (StrUtil.isBlank(url)) {
            // 如果上传文件失败，返回fail字符串
            return SystemConstants.FAIL;
        }
        // TODO 还应该上传到数据库中，把url上传上去
        // 文件上传成功则返回文件路径
        return url;
    }

    @Override
    public FileInfo getFileInfo(String fileKey) {
        try {
            return getBucketManager().stat(BUCKET, fileKey);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return null;
    }

    /**
     * 更新文件类型
     * 这个不会更新文件的真正的类型，需要自己手动修改重新上传
     * TODO 用hutools的ImgUtil.convert()方法修改文件类型
     *
     * @param fileKey     文件关键
     * @param newMimeType 新mime类型
     */
    @Override
    public void updateFileType(String fileKey, String newMimeType) {
        System.out.println("TODO 开发");
        // // 构造一个带指定 Region 对象的配置类
        // Configuration cfg = new Configuration(Region.huadongZheJiang2());
        // Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        // BucketManager bucketManager = new BucketManager(auth, cfg);
        // // 修改文件类型
        // try {
        //     Response response = bucketManager.changeMime(BUCKET, fileKey, newMimeType);
        //     System.out.println(response.bodyString());
        // } catch (QiniuException ex) {
        //     System.out.println(ex.response.toString());
        // }
    }

    @Override
    public void setLifeTime(String fileKey, Integer dayTime) {
        try {
            //过期天数，该文件dayTime天后删除
            getBucketManager().deleteAfterDays(BUCKET, fileKey, dayTime);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }

    @Override
    public FileInfo[] listFileInfo() {
        return listFileInfo("");
    }

    @Override
    public FileInfo[] listFileInfo(String prefix) {
        return listFileInfo(prefix, "");
    }

    @Override
    public FileInfo[] listFileInfo(String prefix, String delimiter) {
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(BUCKET, prefix, limit, delimiter);
        FileInfo[] fileInfos = null;
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            fileInfos = ArrayUtil.addAll(fileListIterator.next(), fileInfos);
        }
        return fileInfos;
    }

    /**
     * 上传到oss
     *
     * @param imageFile 图像文件
     * @param filePath  文件路径
     * @return {@link String}
     */
    private String uploadOss(MultipartFile imageFile, String filePath) {
        //构造一个自动判断 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadongZheJiang2());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            InputStream inputStream = imageFile.getInputStream();
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET);
            try {
                //默认不指定key的情况下，以文件内容的hash值作为文件名
                Response response = uploadManager.put(inputStream, filePath, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    // ignore
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    /**
     * 获取上传凭证
     *
     * @return {@link String}
     */
    private String getUploadToken() {
        return this.auth.uploadToken(BUCKET, null, 3600, putPolicy);
    }
}
