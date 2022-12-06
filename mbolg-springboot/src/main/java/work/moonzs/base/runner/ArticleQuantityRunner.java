package work.moonzs.base.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.entity.Article;
import work.moonzs.service.ArticleService;

import java.util.HashMap;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleQuantityRunner implements CommandLineRunner {
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final RedisCache redisCache;
    private final ArticleService articleService;

    /**
     * 当程序启动时，初始化文章浏览量数据到 redis 中
     *
     * @param args 参数
     * @throws Exception 异常
     */
    @Override
    public void run(String... args) throws Exception {
        threadPoolTaskExecutor.execute(() -> {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            // 只需文章id以及阅读量
            queryWrapper.select(Article::getId, Article::getQuantity);
            List<Article> list = articleService.list(queryWrapper);
            HashMap<String, Object> result = new HashMap<>();
            // 将文章id以及阅读量转化为Map集合
            list.forEach(article -> result.put(article.getId().toString(), article.getQuantity()));
            // 添加到数据缓存中
            redisCache.hmset(CacheConstants.BLOG_VIEWS_QUANTITY, result);
            log.info("成功初始化文章浏览量数据到 redis 中");
        });
    }
}
