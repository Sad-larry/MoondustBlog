package work.moonzs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.domain.vo.sys.SysWebConfigVO;
import work.moonzs.service.*;

import java.util.HashMap;

/**
 * @author Moondust月尘
 */
@Service("homeService")
public class HomeServiceImpl implements HomeService {
    /**
     * 网站配置
     */
    public static final String WEBSITE = "webSite";
    /**
     * 统计数
     */
    public static final String COUNT = "count";
    /**
     * 网页标签图片
     */
    public static final String PAGELIST = "pageList";
    /**
     * 文章数
     */
    public static final String ARTICLE_COUNT = "articleCount";
    /**
     * 分类数
     */
    public static final String CATEGORY_COUNT = "categoryCount";
    /**
     * 标签数
     */
    public static final String TAG_COUNT = "tagCount";
    /**
     * 网站浏览量
     */
    public static final String VIEWS_COUNT = "viewsCount";
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

    @Override
    public HashMap<String, Object> getBlogInfo() {
        HashMap<String, Object> result = new HashMap<>();
        SysWebConfigVO baseWebConfig = webConfigService.getBaseWebConfig();
        result.put(WEBSITE, baseWebConfig);
        result.put(COUNT, getCountData());
        result.put(PAGELIST, webPageService.getPageList());
        return result;
    }

    /**
     * 获得统计数据
     *
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    public HashMap<String, Object> getCountData() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(ARTICLE_COUNT, articleService.articleCount());
        result.put(CATEGORY_COUNT, categoryService.count());
        result.put(TAG_COUNT, tagService.count());
        // TODO 网站浏览量应该用redis缓存存储数据
        result.put(VIEWS_COUNT, 0);
        return result;
    }
}
