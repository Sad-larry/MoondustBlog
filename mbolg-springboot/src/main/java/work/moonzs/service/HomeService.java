package work.moonzs.service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 网站服务接口
 *
 * @author Moondust月尘
 * @since 2022-11-27 11:03:30
 */
public interface HomeService {
    /**
     * 获取redis缓存监控信息
     *
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    Map<String, Object> getCacheInfo();

    /**
     * 获取网站信息
     *
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    HashMap<String, Object> getBlogInfo();

    /**
     * 后台首页图表数据
     * 总访问量、用户数、文章数、留言数
     *
     * @return {@link Map}<{@link String}, {@link Long}>
     */
    Map<String, Long> lineCount();

    /**
     * 访问网站，增加浏览量
     *
     * @param request 请求
     * @return {@link String}
     */
    String visitTheWebsite(HttpServletRequest request);
}
