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
    public static final String IMAGE_TYPE_JEPG = "image/jpeg";
    public static final String IMAGE_TYPE_JPG = "image/jpg";
    public static final String IMAGE_TYPE_PNG = "image/png";
    public static final String IMAGE_TYPE_GIF = "image/gif";
    public static final String IMAGE_TYPE_UNKNOWN = "unknown";

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
        // 图片大小不应超过5MB
        if (file.getSize() > SystemConstants.FILE_SIZE) {
            BusinessAssert.fail(AppHttpCodeEnum.FILE_SIZE_OVERFLOW);
        }
        try (InputStream inputStream = file.getInputStream()) {
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

    public static boolean determiningFile(MultipartFile file) {
        // 文件内容为空
        if (file.isEmpty()) {
            return false;
        }
        // 图片大小不应超过5MB
        if (file.getSize() > SystemConstants.FILE_SIZE) {
            return false;
        }
        // 图片名不能为空
        String filename = file.getOriginalFilename();
        if (filename == null || "".equals(filename)) {
            return false;
        }
        // 图片类型应该符合
        int index = filename.lastIndexOf(".");
        String fileType = filename.substring(index + 1);
        return isLegalFileType(fileType);
    }

    /**
     * 得到图像类型
     *
     * @param mimeType mime类型
     * @return {@link String}
     */
    public static String getImageType(String mimeType) {
        if (IMAGE_TYPE_JEPG.equals(mimeType)) {
            return TYPE_JEPG;
        } else if (IMAGE_TYPE_JPG.equals(mimeType)) {
            return TYPE_JPG;
        } else if (IMAGE_TYPE_PNG.equals(mimeType)) {
            return TYPE_PNG;
        } else if (IMAGE_TYPE_GIF.equals(mimeType)) {
            return TYPE_GIF;
        } else {
            return IMAGE_TYPE_UNKNOWN;
        }
    }
}
