package work.moonzs.base.utils;

import cn.hutool.core.io.FileTypeUtil;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.web.common.BusinessAssert;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Moondust月尘
 */
public class IFileUtil {
    public static final String TYPE_JEPG = "jpeg";
    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_PNG = "png";
    public static final String TYPE_GIF = "gif";

    /**
     * 是合法文件类型,jpeg,jpg,png,gif
     *
     * @param fileType 文件类型
     * @return boolean
     */
    public static boolean isLegalFileType(String fileType) {
        if (TYPE_JEPG.equals(fileType)) {
            return true;
        } else if (TYPE_JPG.equals(fileType)) {
            return true;
        } else if (TYPE_PNG.equals(fileType)) {
            return true;
        } else if (TYPE_GIF.equals(fileType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为合法文件
     * 文件是否为空
     * 文件类型
     * 文件大小
     *
     * @param file 文件
     * @return boolean
     */
    public static boolean isLegalFile(MultipartFile file) {
        // 文件内容为空
        if (file.isEmpty()) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_IS_EMPTY);
        }
        // 图片大小不应超过2MB
        if (file.getSize() > SystemConstants.FILE_SIZE) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_SIZE_OVERFLOW);
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileType = FileTypeUtil.getType(inputStream);
            // 判断文件类型jpeg、jpg、gif、png
            if (!IFileUtil.isLegalFileType(fileType)) {
                // 文件类型不匹配
                BusinessAssert.fail(AppHttpCodeEnum.FILE_TYPE_NOT_MATCH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
