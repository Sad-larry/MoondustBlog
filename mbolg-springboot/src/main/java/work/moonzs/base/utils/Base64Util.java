package work.moonzs.base.utils;

import cn.hutool.core.util.StrUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * base64与图片互转
 */
public class Base64Util {
    private static final String JPG = "jpg";
    private static final String PNG = "png";
    private static final String BMP = "bmp";

    /**
     * 检查有效base64字符串
     *
     * @param data 数据
     * @return {@link String}
     */
    public static boolean checkBase64(String data) {
        return data.startsWith("data:image");
    }

    /**
     * 源字符串截取有效base64字符串
     *
     * @param data 数据
     * @return {@link String}
     */
    public static String data2Base64(String data) {
        return data.substring(data.indexOf(",") + 1);
    }

    /**
     * base64转文件并输出到指定目录
     *
     * @param base64Str base64 str
     * @return {@link String} 文件绝对路径
     */
    public static Map<String, String> decode(String base64Str) {
        HashMap<String, String> result = new HashMap<>();
        // 解码base64转文件
        byte[] b = Base64.getDecoder().decode(replaceEnter(base64Str));
        // 获取随机文件
        String fileName = PathUtil.generateFilePath("." + getImageBase64Type(b));
        result.put("key", fileName);
        // 获取临时地址
        String filePath = PathUtil.getResPhysicalPath();
        File file = new File(StrUtil.appendIfMissing(filePath, File.separator) + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            // 写入临时文件
            bos.write(b);
            result.put("filePath", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取图像base64类型
     *
     * @param b b
     * @return {@link String}
     */
    public static String getImageBase64Type(byte[] b) {
        String type = "";
        if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = BMP;
        } else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = PNG;
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = JPG;
        }
        return type;
    }

    /**
     * 根据图片byte数组转Base64字符串
     *
     * @param image 图像
     * @return {@link String}
     */
    public static String encode(byte[] image) {
        return replaceEnter(Base64.getEncoder().encodeToString(image));
    }

    /**
     * 根据图片uri转Base64字符串
     *
     * @param uri uri
     * @return {@link String}
     */
    public static String encode(String uri) {
        return replaceEnter(Base64.getEncoder().encodeToString(uri.getBytes()));
    }

    /**
     * 图像转byte数组
     *
     * @param path 路径
     * @return {@link byte[]}
     * @path 图片路径
     */
    public static byte[] imageTobyte(String path) {
        byte[] data = null;
        try (FileImageInputStream input = new FileImageInputStream(new File(path)); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 替换输入
     *
     * @param str str
     * @return {@link String}
     */
    public static String replaceEnter(String str) {
        String reg = "[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 判断图片base64字符串的文件格式
     *
     * @param base64ImgData base64 img数据
     * @return {@link String}
     */
    public static String checkImageBase64Format(String base64ImgData) {
        byte[] b = Base64.getDecoder().decode(base64ImgData);
        return getImageBase64Type(b);
    }

    /**
     * PNG格式图片转换为JPG
     *
     * @param pngFilePath png文件路径
     * @param jpgFilePath jpg文件路径
     * @return boolean
     */
    public static boolean png2jpeg(String pngFilePath, String jpgFilePath) {
        File file = new File(pngFilePath);
        boolean flag = false;
        try {
            //png格式转换为jpg格式
            BufferedImage bi = ImageIO.read(file);
            BufferedImage newBufferedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bi, 0, 0, Color.WHITE, null);
            flag = ImageIO.write(newBufferedImage, "jpg", new File(jpgFilePath));
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}

