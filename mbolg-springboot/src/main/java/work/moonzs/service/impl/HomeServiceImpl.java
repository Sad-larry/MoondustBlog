package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.WebConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.vo.sys.SysWebConfigVO;
import work.moonzs.service.*;

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
        // TODO 网站浏览量应该用redis缓存存储数据
        result.put(WebConstants.VIEWS_COUNT, 0);
        return result;
    }
}
