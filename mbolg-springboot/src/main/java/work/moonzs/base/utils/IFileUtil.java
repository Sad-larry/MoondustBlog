package work.moonzs.base.utils;

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
}
