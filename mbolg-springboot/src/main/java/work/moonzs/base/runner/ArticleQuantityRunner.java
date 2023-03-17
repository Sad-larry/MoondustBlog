package work.moonzs.base.runner;

import cn.hutool.core.collection.CollUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     */
    @Override
    public void run(String... args) {
        threadPoolTaskExecutor.execute(() -> {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            // 只需文章id以及阅读量
            queryWrapper.select(Article::getId, Article::getQuantity);
            List<Article> articles = articleService.list(queryWrapper);
            HashMap<String, Object> result = new HashMap<>();
            // 若缓存中有数据则不更新
            Set<String> quantitys = redisCache.hKeys(CacheConstants.BLOG_VIEWS_QUANTITY);
            // 若有删除了的文章，则没必要保存其阅读量
            Set<String> currentKeys = new HashSet<>();
            // 将文章id以及阅读量转化为Map集合
            articles.forEach(article -> {
                if (!quantitys.contains(article.getId().toString())) {
                    // 若缓存中没有对应的key-value，则进行追加
                    result.put(article.getId().toString(), article.getQuantity());
                }
                currentKeys.add(article.getId().toString());
            });
            // subtract([1,2,3,4],[2,3,4,5]) -> [1]
            Object[] delKeys = CollUtil.subtract(quantitys, currentKeys).toArray();
            if (delKeys.length != 0) {
                // 删除缓存多余的数据
                redisCache.hdel(CacheConstants.BLOG_VIEWS_QUANTITY, delKeys);
            }
            if (!result.isEmpty()) {
                // 添加到数据缓存中
                redisCache.hmset(CacheConstants.BLOG_VIEWS_QUANTITY, result);
            }
            log.info("成功初始化文章浏览量数据到 redis 中");
        });
    }
}
