package work.moonzs.base.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moondust月尘
 */
@Component
public class WxmpLoginUtil {
    /**
     * 请求的网址
     */
    private static String WXMP_LOGIN_URL;
    /**
     * 小程序 appId
     */
    public static String WXMP_LOGIN_APPID;
    /**
     * 小程序 appSecret
     */
    public static String WXMP_LOGIN_SECRET;
    /**
     * 授权类型,固定参数
     */
    public static String WXMP_LOGIN_GRANT_TYPE;

    /**
     * 通过code换取微信小程序官网获取的信息
     *
     * @param code 代码
     * @return {@link JSONObject}
     */
    public static JSONObject getResultJson(String code) {
        // 配置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("appid", WXMP_LOGIN_APPID);
        params.put("secret", WXMP_LOGIN_SECRET);
        params.put("js_code", code);
        params.put("grant_type", WXMP_LOGIN_GRANT_TYPE);

        // 向微信服务器发送请求
        String wxRequestResult = HttpUtil.get(WXMP_LOGIN_URL, params);
        return JSONUtil.parseObj(wxRequestResult);
    }

    /**
     * 获取openid，用户唯一标识
     *
     * @param code 代码
     * @return {@link String}
     */
    public static String getOpenid(String code) {
        return getResultJson(code).getStr("openid");
    }

    @Value("${wxmp.loginUrl}")
    public void setWxmpLoginUrl(String wxmpLoginUrl) {
        WXMP_LOGIN_URL = wxmpLoginUrl;
    }

    @Value("${wxmp.loginAppid}")
    public void setWxmpLoginAppid(String wxmpLoginAppid) {
        WXMP_LOGIN_APPID = wxmpLoginAppid;
    }

    @Value("${wxmp.loginSecret}")
    public void setWxmpLoginSecret(String wxmpLoginSecret) {
        WXMP_LOGIN_SECRET = wxmpLoginSecret;
    }

    @Value("${wxmp.loginGrantType}")
    public void setWxmpLoginGrantType(String wxmpLoginGrantType) {
        WXMP_LOGIN_GRANT_TYPE = wxmpLoginGrantType;
    }
}
