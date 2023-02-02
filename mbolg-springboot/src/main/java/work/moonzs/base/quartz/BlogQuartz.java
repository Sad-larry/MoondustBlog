package work.moonzs.base.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.web.service.IOnlineUserService;
import work.moonzs.domain.entity.Article;
import work.moonzs.service.ArticleService;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Moondust月尘
 */
@Component("blogQuartz")
@RequiredArgsConstructor
@Slf4j
public class BlogQuartz {
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final RedisCache redisCache;
    private final ArticleService articleService;
    private final IOnlineUserService iOnlineUserService;

    /**
     * 自动更新文章阅读数
     */
    public void updateReadQuantity() {
        threadPoolTaskExecutor.execute(() -> {
            // 获取 redis 缓存中的文章浏览量数据
            Map<Object, Object> viewsQuantity = redisCache.hmget(CacheConstants.BLOG_VIEWS_QUANTITY);
            ArrayList<Article> articles = new ArrayList<>();
            viewsQuantity.forEach((k, v) -> {
                Article article = new Article();
                // 就。。无语，除了hmget，其他hash表操作返回的都是HashMap<String, Object>，就你特殊，泛型都是Object，一定得改好
                article.setId(Long.parseLong((String) k));
                article.setQuantity((Integer) v);
                articles.add(article);
            });
            // 批量更新数据库中的浏览量
            articleService.updateBatchById(articles);
        });
    }

    /**
     * 清除离线用户
     */
    public void clearOfflineUser() {
        // 默认清除 10 分钟未操作的用户
        clearOfflineUser(10);
    }

    /**
     * 方法参数一定得使用包装类，否则反射找不到该方法
     */
    public void clearOfflineUser(Integer minute) {
        threadPoolTaskExecutor.execute(() -> {
            Long cleanUserCount = iOnlineUserService.userOfflineClean(minute);
            // log.info("清除已过期用户数量: {} 位", cleanUserCount);
        });
    }
}
