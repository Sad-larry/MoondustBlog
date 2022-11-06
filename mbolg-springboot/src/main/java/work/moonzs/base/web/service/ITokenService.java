package work.moonzs.base.web.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.exception.SecurityException;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.entity.LoginUser;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 具体的token验证处理，JwtUtil只能当作工具类，这个是token的具体处理服务
 *
 * @author Moondust月尘
 */
@Component
public class ITokenService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 令牌有效期60分钟
     */
    private static final Long TOTAL_EFFECTIVE_TIME = 7 * 24 * 60 * 60 * 1000L;
    /**
     * 令牌有效期剩余20分钟
     */
    private static final Long LEFT_EFFECTIVE_TIME = 20 * 60 * 1000L;
    /**
     * 设置秘钥明文
     */
    public static final String SECRET_KEY = "this+is+my+moondust+blog";

    /**
     * 通过loginuser创建令牌
     *
     * @param loginUser 登录用户
     * @return {@link String}
     */
    public String createToken(LoginUser loginUser) {
        // 用uuid填充loginuser里面的userUid，作为唯一标识
        String userUid = IdUtil.fastUUID();
        loginUser.setUserUid(userUid);
        refreshToken(loginUser);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(SystemConstants.LOGIN_USER_KEY, userUid);
        return createToken(claims);
    }

    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return {@link LoginUser}
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (StrUtil.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            String userUid = (String) claims.get(SystemConstants.LOGIN_USER_KEY);
            String tokenKey = getTokenKey(userUid);
            return (LoginUser) redisCache.get(tokenKey);
        }
        return null;
    }

    /**
     * 设置登录用户
     *
     * @param loginUser 登录用户
     */
    public void setLoginUser(LoginUser loginUser) {
        if (ObjectUtil.isNotNull(loginUser) && StrUtil.isNotEmpty(loginUser.getUserUid())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除登录用户
     *
     * @param userUid 令牌
     */
    public void delLoginUser(String userUid) {
        if (StrUtil.isNotEmpty(userUid)) {
            redisCache.del(getTokenKey(userUid));
        }
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return secretkey
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, generalKey()).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token).getBody();
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
     * 解析请求，获取token令牌
     *
     * @param request 请求
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录用户
     */
    public boolean verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        // 表示令牌不在有效期内
        if (expireTime - currentTime < 0) {
            // 删除掉用户数据
            String userKey = getTokenKey(loginUser.getUserUid());
            redisCache.del(userKey);
            return false;
        }
        if (expireTime - currentTime <= LEFT_EFFECTIVE_TIME) {
            refreshToken(loginUser);
        }
        return true;
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        // 在当前更新的登录时间基础上再延长60分钟
        loginUser.setExpireTime(loginUser.getLoginTime() + TOTAL_EFFECTIVE_TIME);
        // 根据id将loginUser重新缓存
        String userKey = getTokenKey(loginUser.getUserUid());
        redisCache.set(userKey, loginUser);
    }

    /**
     * 获得令牌密钥
     *
     * @param userUid 用户uid
     * @return {@link String}
     */
    private String getTokenKey(String userUid) {
        return CacheConstants.LOGIN_USER_KEY + userUid;
    }
}
