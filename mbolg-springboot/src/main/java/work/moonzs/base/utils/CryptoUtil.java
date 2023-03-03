package work.moonzs.base.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 用于加密解密工具
 *
 * @author Moondust月尘
 */
public class CryptoUtil {
    /**
     * 密钥
     */
    private static final String key = "!#@$#%$^%&^*&(*)";
    /**
     * 构建
     */
    private static final SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes());
    /**
     * 密钥-对应密钥AES对称加密算法
     */
    private static final HashMap<String, AES> aesMap = new HashMap<>();

    /**
     * 加密十六进制
     *
     * @param decryptStr 解密str
     * @return {@link String}
     */
    public static String encryptHex(String decryptStr) {
        return aes.encryptHex(decryptStr);
    }

    /**
     * 解密str
     *
     * @param encryptHex 加密十六进制
     * @return {@link String}
     */
    public static String decryptStr(String encryptHex) {
        return aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * aes算法将数据加密成十六进制
     *
     * @param key  关键
     * @param data 数据
     * @return {@link String}
     */
    public static String aesEncryptHex(String key, String data) {
        if (!aesMap.containsKey(key)) {
            aesMap.put(key, SecureUtil.aes());
        }
        return aesMap.get(key).encryptHex(data, StandardCharsets.UTF_8);
    }

    /**
     * aes算法解密字符串
     *
     * @param key  关键
     * @param data 数据
     * @return {@link String}
     */
    public static String aesDecryptStr(String key, String data) {
        if (!aesMap.containsKey(key)) {
            throw new IllegalArgumentException("不存在这样的 KEY");
        }
        return aesMap.get(key).decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }
}
