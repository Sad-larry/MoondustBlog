package work.moonzs.base.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 用于加密解密工具
 *
 * @author Moondust月尘
 */
public class CryptoUtil {
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKw79JNltlUVYxBQ1tKPMUj61/8AtQ/cCmO/6MH5Xi86Gmve0G2y7XFaB4+U+gSW5YpyHimGmJAN7NMpCg1+jy8CAwEAAQ==";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEArDv0k2W2VRVjEFDW0o8xSPrX/wC1D9wKY7/owfleLzoaa97QbbLtcVoHj5T6BJblinIeKYaYkA3s0ykKDX6PLwIDAQABAkAegPPdExuK0CHeZuLVIeUxrrOIh0CKEYeJiRiZGigzJsWg14vVrGY1qXRasRbJHw6vggx1f5OHSEWxLTvgtYsxAiEA4zll20rWvVNJADhOye1btOA4BLPLSPSWbD2C5KotejkCIQDCC8h0CiFsWZ/LaZe3kvHhoIdAOL88fPfd5GOrSgJ0pwIhAIWpIuBvUohF8KA/fyFLDXIFnw4tEPyWW9HKETAfZucJAiAAsuIp6M9uAHSe2uZ89r6APX+/L3Ug1qJd3jCtsTqJCQIgZDXLUK6FPmWBFGEnycY8N6c7wyBeck+Q9S+0giJPx/o=";

    /**
     * RSA公钥加密
     *
     * @param str 需要加密的字符串
     * @return 公钥加密后的内容
     */
    public static String encrypt(String str) {
        String outStr = null;
        try {
            // base64编码的公钥
            byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ignored) {
        }
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @return 私钥解密后的内容
     */
    public static String decrypt(String str) {
        String outStr = null;
        try {
            // 64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
            // base64编码的私钥
            byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (Exception ignored) {
        }
        return outStr;
    }

}
