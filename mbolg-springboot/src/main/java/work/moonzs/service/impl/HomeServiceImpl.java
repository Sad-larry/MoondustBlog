package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.enums.WebConstants;
import work.moonzs.base.utils.IpUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.vo.sys.SysWebConfigVO;
import work.moonzs.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Moondust月尘
 */
@Service("homeService")
public class HomeServiceImpl implements HomeService {
    @Autowired
    private WebConfigService webConfigService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private WebPageService webPageService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserLogService userLogService;

    @Override
    public Map<String, Object> getCacheInfo() {
        RedisTemplate<String, Object> redisTemplate = redisCache.getRedisTemplate();
        // 获取redis缓存完整信息
        //Properties info = redisTemplate.getRequiredConnectionFactory().getConnection().info();
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        // 获取redis缓存命令统计信息
        //Properties commandStats = redisTemplate.getRequiredConnectionFactory().getConnection().info("commandstats");
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        // 获取redis缓存中可用键Key的总数
        //Long dbSize = redisTemplate.getRequiredConnectionFactory().getConnection().dbSize();
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);
        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }

    @Override
    public HashMap<String, Object> getBlogInfo() {
        HashMap<String, Object> result = new HashMap<>();
        SysWebConfigVO baseWebConfig = webConfigService.getBaseWebConfig();
        result.put(WebConstants.WEBSITE, baseWebConfig);
        result.put(WebConstants.COUNT, getCountData());
        result.put(WebConstants.PAGELIST, webPageService.getPageList());
        return result;
    }

    /**
     * 获得统计数据
     *
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    public HashMap<String, Object> getCountData() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(WebConstants.ARTICLE_COUNT, articleService.articleCount());
        result.put(WebConstants.CATEGORY_COUNT, categoryService.count());
        result.put(WebConstants.TAG_COUNT, tagService.count());
        // 网站浏览量应该用redis缓存存储数据
        result.put(WebConstants.VIEWS_COUNT, getTotalVisits());
        return result;
    }

    @Override
    public Map<String, Long> lineCount() {
        Map<String, Long> result = new HashMap<>();
        result.put(WebConstants.TOTAL_VISITS, getTotalVisits());
        result.put(WebConstants.TOTAL_USERS, userService.count());
        result.put(WebConstants.TOTAL_ARTICLES, articleService.count());
        result.put(WebConstants.TOTAL_MESSAGES, messageService.count());
        return result;
    }

    @Override
    public String visitTheWebsite(HttpServletRequest request) {
        // 获取ip
        String ipAddress = IpUtil.getIpAddr(request);
        // 获取访问设备
        UserAgent userAgent = IpUtil.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + userAgent.getOs().getName();
        String md5 = DigestUtil.md5Hex(uuid.getBytes());
        // 判断是否访问
        if (!redisCache.sIsMember(CacheConstants.UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtil.getCityInfo(ipAddress);
            if (StrUtil.isNotBlank(ipSource)) {
                if (ipSource.startsWith("中国")) {
                    ipSource = ipSource.split("\\|")[1]
                            .replaceAll(SystemConstants.PROVINCE, "")
                            .replaceAll(SystemConstants.CITY, "");
                } else {
                    ipSource = ipSource.split("\\|")[0];
                }
                redisCache.hincr(CacheConstants.VISITOR_AREA, ipSource, 1);
            } else {
                redisCache.hincr(CacheConstants.VISITOR_AREA, SystemConstants.UNKNOWN, 1L);
            }
            // 访问量+1
            redisCache.incr(CacheConstants.BLOG_VISITS_COUNT, 1L);
            // 保存唯一标识
            redisCache.sSet(CacheConstants.UNIQUE_VISITOR, md5);
        }
        return null;
    }

    @Override
    public Map<String, Object> initChart() {
        Map<String, Object> result = new HashMap<>();
        // 访问量、用户数、文章数、留言数数据
        result.put("groupCountData", lineCount());
        // 博客贡献图
        result.put("blogContributeCount", articleService.getBlogContributeCount());
        // 文章阅读量排行，排名从高到底
        result.put("blogReadVolume", articleService.getBlogReadVolume());
        // 文章分类统计，获取分类在文章中使用的数量
        result.put("blogCategory", categoryService.listWebCategory());
        // 文章标签统计
        result.put("blogTag", tagService.listWebTag());
        // 一周访问量
        result.put("weeklyVisits", userLogService.getWeeklyVisits());
        return result;
    }


    /**
     * 获取网站访问量
     *
     * @return {@link Long}
     */
    private Long getTotalVisits() {
        Object count = redisCache.get(CacheConstants.BLOG_VISITS_COUNT);
        return ObjUtil.isNull(count) ? 0L : Long.parseLong(count.toString());
    }
}
