package work.moonzs.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Moondust月尘
 */
public class PathUtil {
    /**
     * 简单随机uuid
     *
     * @return {@link String}
     */
    public static String simpleRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成文件路径
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public static String generateFilePath(String fileName) {
        // 根据日期生成路径     xxxx/xx/xx/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        // uuid 作为文件名
        String uuid = simpleRandomUUID();
        // 后缀和文件名一样
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        return datePath + uuid + fileType;
    }

    /**
     * 随机md名字
     *
     * @return {@link String}
     */
    public static String getUUIDMdName() {
        return simpleRandomUUID() + ".md";
    }
}
