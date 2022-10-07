package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.Image;
import work.moonzs.mapper.ImageMapper;
import work.moonzs.service.UploadService;
import work.moonzs.base.utils.PathUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Moondust月尘
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
    @Value("${oss.qiniu.domain-url}")
    private String DOMAIN_URL;
    @Value("${oss.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${oss.qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${oss.qiniu.bucket}")
    private String BUCKET;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 上传图片具体实现方法
     *
     * @param image 图像
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Override
    public ResponseResult<?> uploadImage(MultipartFile image) {
        // 获取初始文件名
        String originalFilename = image.getOriginalFilename();
        // 通过
        String filePath = PathUtils.generateFilePath(originalFilename);
        // 如果判断通过上传到OSS
        String url = uploadOss(image, filePath);
        if (StrUtil.isBlank(url)) {
            return ResponseResult.fail();
        }
        // 还应该上传到数据库中，把filePath上传上去而不是url
        Image sqlImage = new Image();
        sqlImage.setImageName(originalFilename);
        sqlImage.setImageSrc(filePath);
        imageMapper.insert(sqlImage);
        return ResponseResult.success(url);
    }

    /**
     * 上传oss
     *
     * @param imageFile 图像文件
     * @param filePath  文件路径
     * @return {@link String}
     */
    private String uploadOss(MultipartFile imageFile, String filePath) {
        //构造一个自动判断 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imageFile.getInputStream();
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return DOMAIN_URL + key;
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
}
