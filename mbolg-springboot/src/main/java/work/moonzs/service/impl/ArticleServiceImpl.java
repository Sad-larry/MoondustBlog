package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Article;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.service.ArticleService;

/**
 * (Article)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:17
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}

