package work.moonzs.controller.system;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.IFileUtil;
import work.moonzs.base.utils.PathUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.service.IQiNiuService;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO 这个控制层不需要，过会儿就删
 *
 * @author Moondust月尘
 */
@RestController("SystemUploadC")
@RequestMapping("/system")
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String fileTempPath;
    @Value("${oss.qiniu.domain}")
    private String domain;

    private final IQiNiuService qiNiuService;

    public UploadController(IQiNiuService qiNiuService) {
        this.qiNiuService = qiNiuService;
    }

    /**
     * 上传图片
     * 在springboot-demo里面，将文件先保存在临时文件里面，再直接上传，现在我不需要
     *
     * @param file 文件
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            // 文件内容为空
            return ResponseResult.fail(AppHttpCodeEnum.FILE_IS_EMPTY);
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileType = FileTypeUtil.getType(inputStream);
            // 判断文件类型jpeg、jpg、gif、png
            if (!IFileUtil.isLegalFileType(fileType)) {
                // 文件类型不匹配
                return ResponseResult.fail(AppHttpCodeEnum.FILE_TYPE_NOT_MATCH);
            }
            // TODO 判断文件大小
            String filePath = PathUtil.generateFilePath(file.getOriginalFilename());
            Response response = qiNiuService.uploadFile(file, filePath);
            if (response.isOK()) {
                JSONObject jsonObject = JSONUtil.parseObj(response.bodyString());
                String yunFileName = jsonObject.getStr("key");
                String yunFilePath = StrUtil.appendIfMissing(domain, "/") + yunFileName;
                // TODO 将数据yunFileName存储到数据库中
                return ResponseResult.success(AppHttpCodeEnum.FILE_UPLOAD_SUCCESS, yunFilePath);
            } else {
                return ResponseResult.fail(AppHttpCodeEnum.FILE_UPLOAD_FAIL);
            }
        } catch (IOException e) {
            return ResponseResult.fail(AppHttpCodeEnum.NOT_FIND_SPECIFIED_PATH);
        }
    }
}
