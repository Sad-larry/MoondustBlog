package work.moonzs.base.utils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.exception.SecurityException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author Moondust月尘
 */
public class JwtUtil {
    /**
     * 有效期为 24 * 60 * 60 * 1000  一个小时不够，一天
     */
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;
    /**
     * 令牌有效期剩余20分钟
     */
    public static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * 设置秘钥明文
     */
    public static final String JWT_KEY = "moondust";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jtw
     *
     * @param subject token中要存放的数据（json格式）
     * @return string
     */
    public static String createJWT(String subject) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成jtw
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return string
     */
    public static String createJWT(String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                // 唯一的ID
                .setId(uuid)
                // 主题  可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer("md")
                // 签发时间
                .setIssuedAt(now)
                // 使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 创建jwt
     *
     * @param id        id
     * @param subject   主题
     * @param ttlMillis 时间
     * @return {@link String}
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwt jwt
     * @return boolean
     */
    public static boolean checkToken(String jwt) {
        if (StrUtil.isNotEmpty(jwt)) {
            Claims claims = null;
            try {
                claims = parseJWT(jwt);
                String subject = claims.getSubject();
                return StrUtil.isNotEmpty(subject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return secretkey
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析jwt
     *
     * @param jwt jwt
     * @return {@link Claims}
     */
    public static Claims parseJWT(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(generalKey())
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // Token 已过期
            throw new SecurityException(AppHttpCodeEnum.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            // 不支持的 Token
            throw new SecurityException(AppHttpCodeEnum.TOKEN_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            // Token 无效
            throw new SecurityException(AppHttpCodeEnum.TOKEN_INVALID);
        } catch (SignatureException e) {
            // 无效的 Token 签名
            throw new SecurityException(AppHttpCodeEnum.TOKEN_SIGNATURE_INVALID);
        } catch (IllegalArgumentException e) {
            // Token 参数不存在
            throw new SecurityException(AppHttpCodeEnum.TOKEN_NO_SUCH_PARAMETER);
        }
    }

    /**
     * 获取token的创建时间
     *
     * @param jwt jwt
     * @return {@link Date}
     */
    public static Date getIssueAt(String jwt) {
        return parseJWT(jwt).getIssuedAt();
    }

    /**
     * 解析请求，获取token令牌
     *
     * @param request 请求
     */
    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

