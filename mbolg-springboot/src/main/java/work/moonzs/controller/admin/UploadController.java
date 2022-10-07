package work.moonzs.controller.admin;

import cn.hutool.core.io.FileTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.domain.ResponseResult;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.service.UploadService;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Moondust月尘
 */
@RestController(value = "AdminUploadC")
@RequestMapping("/admin")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping("/upload/image")
    public ResponseResult<?> uploadImage(@RequestParam("file") MultipartFile image) throws IOException {
        // 判断文件类型jpg、gif、png
        InputStream inputStream = image.getInputStream();
        String type = FileTypeUtil.getType(inputStream);
        if (!"jpg".equals(type) && !"gif".equals(type) && !"png".equals(type)) {
            return ResponseResult.fail(AppHttpCodeEnum.FILE_TYPE_NOT_MATCH);
        }
        // TODO 判断文件大小
        return uploadService.uploadImage(image);
    }
}
