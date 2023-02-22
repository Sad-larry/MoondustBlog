package work.moonzs.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Moondust月尘
 */
@Slf4j
public class IHttpUtil {
    /**
     * 验证网络资源是否有效
     *
     * @param url url
     * @return boolean
     */
    public static boolean checkUrl(String url) {
        try {
            URL urlStr = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlStr.openConnection();
            int state = connection.getResponseCode();
            return state == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            log.error("Description Failed to verify network resources!");
        }
        return false;
    }
}
