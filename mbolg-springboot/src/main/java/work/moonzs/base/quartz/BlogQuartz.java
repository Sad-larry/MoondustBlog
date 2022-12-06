package work.moonzs.base.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
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
}
