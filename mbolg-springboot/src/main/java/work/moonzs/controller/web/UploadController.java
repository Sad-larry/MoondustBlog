package work.moonzs.controller.web;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.annotation.WebOperationLogger;
import work.moonzs.base.qiniu.service.QiniuService;
import work.moonzs.base.utils.Base64Util;
import work.moonzs.domain.ResponseResult;

import java.util.Map;

/**
 * @author Moondust月尘
 */
@RestController("WebUploadC")
@RequestMapping("/web/upload")
@RequiredArgsConstructor
public class UploadController {

    private final QiniuService qiniuService;

    /**
     * 上传微信小程序用户头像
     *
     * @param avatarBase64 base64字符串
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "上传微信小程序用户头像")
    @WebOperationLogger(value = "上传模块-上传头像", type = "添加", desc = "用户头像")
    @PostMapping(path = "/wxmpAvatar")
    public ResponseResult wxmpAvatar(@RequestParam String avatarBase64) {
        if (Base64Util.checkBase64(avatarBase64)) {
            // 提取主要数据
            String base64Str = Base64Util.data2Base64(avatarBase64);
            // 对数据进行解码，并保存到临时文件中
            Map<String, String> decode = Base64Util.decode(base64Str);
            qiniuService.uploadFile(decode.get("key"), decode.get("filePath"));
            // 删除临时文件
            FileUtil.del(decode.get("filePath"));
            // 返回图片下载地址
            return ResponseResult.success(qiniuService.publicDownload(decode.get("key")));
        }
        return ResponseResult.fail();
    }
}
