package work.moonzs.base.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * Ip 地址解析工具类
 *
 * @author Moondust月尘
 */
@Slf4j
public class IpUtil {
    private static Searcher searcher;

    static {
        // 从数据库路径中读取数据缓存
        // byte[] cbuff = Searcher.loadContentFromFile(dbPath);
        ClassPathResource classPathResource = new ClassPathResource("ip2region.xdb");
        // 通过输入流的方式创建一个完全基于内存的查询对象
        try (InputStream is = classPathResource.getInputStream();) {
            byte[] cBuff = is.readAllBytes();
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (IOException e) {
            log.error("failed to create content cached searcher: {}", e.toString());
        }
    }

    /**
     * 通过 httprequest 获取 IP 地址
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 本机访问
        if ("localhost".equalsIgnoreCase(ipAddress) || "127.0.0.1".equalsIgnoreCase(ipAddress) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ipAddress && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 15) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
    
    /**
     * 获取 IP 地址归属地
     */
    public static String getCityInfo(String ip) {
        // 若初始化失败，则返回空
        if (null == searcher) {
            return null;
        }
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

    /**
     * 获取mac地址
     */
    public static String getMacAddress() throws Exception {
        // 取mac地址
        byte[] macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macAddressBytes.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().trim().toUpperCase();
    }
}
