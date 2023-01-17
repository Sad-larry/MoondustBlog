package work.moonzs.base.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

/**
 * Ip 地址解析工具类
 *
 * @author Moondust月尘
 */
@Slf4j
public class IpUtil {
    /**
     * 数据库路径
     */
    private final static String dbPath;
    private static Searcher searcher;
    private static byte[] vIndex;
    private final static String localIp = "127.0.0.1";

    static {
        // 1、获取 ip2region.xdb 离线数据库路径
        dbPath = Objects.requireNonNull(IpUtil.class.getResource("/ip2region.xdb")).getPath();
        try {
            // 2、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
            vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        } catch (Exception e) {
            log.error("failed to load vector index from `{}`: {}", dbPath, e);
        }
        try {
            // 3、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (IOException e) {
            log.error("failed to create vectorIndex cached searcher with `{}`: {}", dbPath, e);
        }
    }

    /**
     * 通过 httprequest 获取 IP 地址
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("X-Forwarded-For");
            if (!checkIpAddressIsEmpty(ipAddress)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ipAddress.contains(",")) {
                    ipAddress = ipAddress.split(",")[0];
                }
                // 如果是本地 ip，则根据网卡获取本机 ip
                if (localIp.equals(ipAddress)) {
                    InetAddress localHost = InetAddress.getLocalHost();
                    ipAddress = localHost.getHostAddress();
                }
            }
            if (checkIpAddressIsEmpty(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (checkIpAddressIsEmpty(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (checkIpAddressIsEmpty(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (checkIpAddressIsEmpty(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IpUtil ERROR :", e);
        }
        return ipAddress;
    }

    /**
     * 检查 IP 地址是否为空
     *
     * @param ipAddress ip地址
     * @return boolean
     */
    private static boolean checkIpAddressIsEmpty(String ipAddress) {
        return ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress);
    }

    /**
     * 获取 IP 地址归属地
     */
    public static String getCityInfo(String ip) {
        try {
            // 通过 searcher 查询 IP
            String ipInfo = searcher.search(ip);
            if (!StrUtil.isEmpty(ipInfo)) {
                // 国外部分数据只定位到国家，后面地址为 |0|，所以需要替换掉
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }
            return ipInfo;
        } catch (Exception e) {
            log.error("failed to search({}): {}", ip, e);
        }
        return null;
    }
}
