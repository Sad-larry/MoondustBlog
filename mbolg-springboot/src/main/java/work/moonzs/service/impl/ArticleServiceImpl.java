package work.moonzs.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.utils.PathUtil;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysArticleReadVO;
import work.moonzs.domain.vo.sys.SysUploadArticleVO;
import work.moonzs.domain.vo.web.ArticleBaseVO;
import work.moonzs.domain.vo.web.ArticleInfoVO;
import work.moonzs.domain.vo.web.ArticlePreviewVO;
import work.moonzs.mapper.ArticleMapper;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.ArticleService;
import work.moonzs.service.CategoryService;
import work.moonzs.service.CommentService;
import work.moonzs.service.TagService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 博客文章表(Article)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:38:42
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    /**
     * 文件类型为 Markdown 类型
     */
    public static final String FILE_TYPE_MD = "text/markdown";

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public boolean publishArticle(ArticleDTO articleDTO) {
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        article.setUserId(SecurityUtil.getUserId());
        // 查询分类名是否存在，存在则返回ID，否则新增分类再返回ID
        article.setCategoryId(categoryService.insertCategoryWithName(articleDTO.getCategoryName()));
        // 插入文章，若新增成功则插入文章标签关联表
        int insert = getBaseMapper().insert(article);
        if (insert > 0) {
            List<Long> tagList = getTagList(articleDTO.getTags());
            return tagMapper.saveTagsWithArticleId(article.getId(), tagList);
        }
        return false;
    }

    /**
     * 通过标签名，查询所有标签ID，并插入新的标签
     *
     * @param tags 标签
     * @return {@link List}<{@link Long}>
     */
    private List<Long> getTagList(List<String> tags) {
        List<Long> tagIds = new ArrayList<>();
        tags.forEach(name -> {
            if (null == name) {
                return;
            }
            Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
            if (tag == null) {
                tag = Tag.builder().name(name).build();
                tagMapper.insert(tag);
            }
            tagIds.add(tag.getId());
        });
        return tagIds;
    }

    @Override
    public PageVO<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Object> page = new Page<>(pageNum, pageSize);
        List<ArticleVo> articleVos = getBaseMapper().listArticle(page, fuzzyField);
        return new PageVO<>(articleVos, page.getTotal());
    }

    @Override
    public ArticleVo getArticleById(Long articleId) {
        ArticleVo articleVo = getBaseMapper().getArticleById(articleId);
        BusinessAssert.notNull(articleVo, AppHttpCodeEnum.BLOG_NOT_EXIST);
        articleVo.setTagNames(tagMapper.getTagsByArticleId(articleId));
        return articleVo;
    }

    @Override
    public boolean updateArticle(ArticleDTO articleDTO) {
        validateArticle(articleDTO.getId());
        Article article = BeanCopyUtil.copyBean(articleDTO, Article.class);
        article.setCategoryId(categoryService.insertCategoryWithName(articleDTO.getCategoryName()));
        updateById(article);
        // 文章标签修改，先删除所有标签再新增标签
        tagMapper.removeTagsByArticleIds(new Long[]{article.getId()});
        List<Long> tagList = getTagList(articleDTO.getTags());
        tagMapper.saveTagsWithArticleId(article.getId(), tagList);
        return true;
    }

    @Override
    public boolean updateArticleCategory(Long articleId, String categoryName) {
        validateArticle(articleId);
        Article article = new Article();
        article.setId(articleId);
        article.setCategoryId(categoryService.insertCategoryWithName(categoryName));
        return updateById(article);
    }

    @Override
    public boolean updateArticleTags(Long articleId, List<String> tags) {
        validateArticle(articleId);
        tagMapper.removeTagsByArticleIds(new Long[]{articleId});
        List<Long> tagList = getTagList(tags);
        tagMapper.saveTagsWithArticleId(articleId, tagList);
        return true;
    }

    @Override
    public boolean deleteArticle(Long[] articleIds) {
        tagMapper.removeTagsByArticleIds(articleIds);
        return removeBatchByIds(List.of(articleIds));
    }

    /**
     * 验证文章符合性
     *
     * @param articleId 文章id
     */
    public void validateArticle(Long articleId) {
        Article byId = getById(articleId);
        // 是否存在博客
        BusinessAssert.notNull(byId, AppHttpCodeEnum.BLOG_NOT_EXIST);
        // 若当前用户修改的不是自己的文章则发出警告
        if (!SecurityUtil.getUserId().equals(byId.getUserId())) {
            BusinessAssert.fail(AppHttpCodeEnum.WARNING_ARTICLE_BAD_REQUEST);
        }
    }

    @Override
    public boolean topArticle(Long articleId, Integer isStick) {
        LambdaUpdateWrapper<Article> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(Article::getIsStick, isStick);
        queryWrapper.eq(Article::getId, articleId);
        return update(queryWrapper);
    }

    // ----- 前端接口部分 -----

    @Override
    public Long articleCount() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublish, StatusConstants.NORMAL);
        return count(queryWrapper);
    }

    @Override
    public PageVO<ArticlePreviewVO> listWebArticle(Integer pageNum, Integer pageSize, Long categoryId, Long tagId) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        List<ArticlePreviewVO> articlePreviews = baseMapper.listPreviewPage(page, categoryId, tagId, null);
        articlePreviews.forEach(item -> {
            item.setTagVOList(tagService.getBlogTagsByArticleId(item.getId()));
        });
        return new PageVO<>(articlePreviews, page.getTotal());
    }

    @Override
    public ArticleInfoVO getArticleInfo(Long articleId) {
        // 根据 ID 查询文章
        Article byId = getById(articleId);
        if (byId == null) {
            BusinessAssert.fail(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 返回的数据视图类
        ArticleInfoVO blogArticle = BeanCopyUtil.copyBean(byId, ArticleInfoVO.class);
        // 设置分类
        blogArticle.setCategory(categoryService.getBlogCategoryById(blogArticle.getCategoryId()));
        // 设置标签
        blogArticle.setTagList(tagService.getBlogTagsByArticleId(blogArticle.getId()));
        // 评论
        blogArticle.setComments(commentService.listArticleComment(blogArticle.getId()));
        // 最新文章
        blogArticle.setNewestArticleList(getNewestArticles(blogArticle.getId()));
        // 查询上一篇下一篇文章
        blogArticle.setLastArticle(getNextOrLastArticle(blogArticle.getId(), StatusConstants.LAST_ARTICLE));
        blogArticle.setNextArticle(getNextOrLastArticle(blogArticle.getId(), StatusConstants.NEXT_ARTICLE));
        // 相关推荐
        blogArticle.setRecommendArticleList(listRecommendArticle(blogArticle.getId()));
        // 增加文章点赞量
        // threadPoolTaskExecutor.execute(() -> incr(blogArticle.getId(), CacheConstants.BLOG_LIKE_QUANTITY));
        // 异步 增加文章阅读量
        threadPoolTaskExecutor.execute(() -> incr(blogArticle.getId(), CacheConstants.BLOG_VIEWS_QUANTITY));
        return blogArticle;
    }

    @Override
    public List<ArticleBaseVO> getNewestArticles(Long articleId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getAvatar, Article::getCreateTime)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                .ne(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last(StatusConstants.LIMIT_FIVE);
        return BeanCopyUtil.copyBeanList(list(queryWrapper), ArticleBaseVO.class);
    }

    @Override
    public ArticleBaseVO getNextOrLastArticle(Long articleId, Integer type) {
        boolean queryLast = StatusConstants.LAST_ARTICLE.equals(type);
        boolean queryNext = StatusConstants.NEXT_ARTICLE.equals(type);
        // 查询上一篇文章和查询下一篇文章，同为真或同为假则返回失败
        if (queryLast == queryNext) {
            BusinessAssert.fail(AppHttpCodeEnum.BLOG_NOT_EXIST);
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getAvatar)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                // 当查询上一篇文章时，条件符合 id < this.id
                .lt(queryLast, Article::getId, articleId)
                .gt(queryNext, Article::getId, articleId)
                // 当查询上一篇文章，条件符合 id 依次递减找最大值，否则 id 依次递增找最小值
                .orderByDesc(queryLast, Article::getId)
                .orderByAsc(queryNext, Article::getId)
                .last(StatusConstants.LIMIT_ONE);
        // 当有多个或没有时不抛出异常
        Article one = getOne(queryWrapper, false);
        return ObjUtil.isNotNull(one) ? BeanCopyUtil.copyBean(one, ArticleBaseVO.class) : null;
    }

    @Override
    public List<ArticleBaseVO> listRecommendArticle(Long articleId) {
        return baseMapper.listRecommendArticle(articleId);
    }

    @Override
    public PageVO<ArticleBaseVO> getArchives(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateTime)
                .eq(Article::getIsPublish, StatusConstants.NORMAL)
                .orderByDesc(Article::getCreateTime);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), ArticleBaseVO.class), page.getTotal());
    }

    @Override
    public SysUploadArticleVO uploadArticle(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            BusinessAssert.fail("文件不能为空");
        }
        String filename = file.getOriginalFilename();
        int i = filename.lastIndexOf(".");
        String contentType = filename.substring(i + 1);
        // 判断文件类型
        if (!"md".equals(contentType)) {
            // if (!FILE_TYPE_MD.equals(contentType)) {
            BusinessAssert.fail("文件类型不匹配，需要 .md 文件格式");
        }
        // 将文件保存为临时文件，能进来的必为md文件
        String fileName = PathUtil.getUUIDMdName();
        // 临时目录
        String localFilePath = StrUtil.appendIfMissing(PathUtil.getResPhysicalPath(), File.separator);
        File saveFile = new File(localFilePath, fileName);
        try {
            // 保存至文件
            file.transferTo(saveFile);
            // 解析文件
            SysUploadArticleVO parseResult = parseFile(saveFile, file.getOriginalFilename().replaceAll(".md", ""));
            // 解析完毕后删除文件
            if (saveFile.exists()) {
                // TODO 如果文件删除失败，则加入日志中
                saveFile.delete();
            }
            // 返回解析结果
            return parseResult;
        } catch (IOException e) {
            BusinessAssert.fail("文件上传失败");
        }
        return null;
    }

    @Override
    public Map<String, Object> getBlogContributeCount() {
        // 获取截止今天结束日期
        DateTime endDateTime = DateUtil.date();
        String endTime = endDateTime.toDateStr();
        // 获取当前时间前一年的日期
        DateTime startDateTime = DateUtil.offset(DateUtil.date(), DateField.YEAR, -1);
        String startTime = startDateTime.toDateStr();
        // 获取博客贡献度数据
        List<Map<String, Object>> blogContributeMap = baseMapper.listBlogContributeCount(startTime, endTime);
        // 获取时间区间集合，时间单位为 天
        List<DateTime> dateList = DateUtil.rangeToList(startDateTime, endDateTime, DateField.DAY_OF_YEAR);
        Map<String, Object> dateMap = new HashMap<>();
        // 读取博客贡献的数据，格式为 具体日期-数量
        for (Map<String, Object> itemMap : blogContributeMap) {
            dateMap.put(itemMap.get("date").toString(), itemMap.get("count"));
        }
        List<List<Object>> resultList = new ArrayList<>();
        for (DateTime item : dateList) {
            int count = 0;
            // 若有数据，则加入数据
            if (dateMap.get(item.toDateStr()) != null) {
                count = Integer.parseInt(dateMap.get(item.toDateStr()).toString());
            }
            List<Object> objectList = new ArrayList<>();
            objectList.add(item.toDateStr());
            objectList.add(count);
            resultList.add(objectList);
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<String> contributeDateList = new ArrayList<>();
        contributeDateList.add(startTime);
        contributeDateList.add(endTime);
        resultMap.put("contributeDate", contributeDateList);
        resultMap.put("contributeCount", resultList);
        return resultMap;
    }

    @Override
    public List<SysArticleReadVO> getBlogReadVolume() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 获取文章Id方便链接到前端详细页面，文章标题以及阅读量
        queryWrapper.select(Article::getId, Article::getTitle, Article::getQuantity);
        queryWrapper.eq(Article::getIsPublish, StatusConstants.NORMAL);
        queryWrapper.eq(Article::getIsSecret, StatusConstants.DISABLE);
        queryWrapper.orderByDesc(Article::getQuantity);
        queryWrapper.last(StatusConstants.LIMIT_TEN);
        List<Article> list = list(queryWrapper);
        return BeanCopyUtil.copyBeanList(list, SysArticleReadVO.class);
    }

    @Override
    public PageVO<ArticlePreviewVO> searchArticles(String keywords) {
        Page<Article> page = new Page<>(1, 10);
        List<ArticlePreviewVO> articlePreviews = baseMapper.listPreviewPage(page, null, null, keywords);
        articlePreviews.forEach(item -> {
            item.setTagVOList(tagService.getBlogTagsByArticleId(item.getId()));
        });
        return new PageVO<>(articlePreviews, page.getTotal());
    }

    /**
     * 解析文件
     *
     * @param file 文件
     * @return {@link SysUploadArticleVO}
     */
    private SysUploadArticleVO parseFile(File file, String originalFileName) {
        // 读取文件
        try (FileReader fileReader = new FileReader(file)) {
            char[] chs = new char[1024];
            int len;
            // 存储文件的内容
            StringBuilder str = new StringBuilder();
            while ((len = fileReader.read(chs)) != -1) {
                str.append(new String(chs, 0, len));
            }
            // 提取图片的正则表达式
            String regex = "!\\[(.*?)\\]\\((.*?)\\)";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 指定要匹配的字符串
            Matcher matcher = pattern.matcher(str);
            // sb为替换图片链接后的内容
            StringBuilder sb = new StringBuilder();
            // imageLink的key值为需要上传的图片名称，value为新上传的图片名称
            HashMap<String, Object> imageLink = new HashMap<>();
            // 将文件中的图片URI取出，并读取相关文件上传到七牛云，该图片URI被替换为七牛云图片链接
            while (matcher.find()) {
                // 匹配成功的字符串
                String group = matcher.group();
                // 存有本地文件路径的字符串 -> imgs/2022-12-5.jpg "测试文件上传"
                String $2 = group.replaceAll(regex, "$2");
                // 如果是网络图片资源，则跳过
                if ($2.startsWith("http://") || $2.startsWith("https://")) {
                    continue;
                }
                // 提取图片名，并创建新的图片链接，使用map对应
                String imageName = getImageName($2);
                boolean isImage = checkImageName(imageName);
                // 不为图片格式则跳过
                if (!isImage) {
                    continue;
                }
                // 获取新的随机名字
                String newImageName = PathUtil.generateFilePath(imageName);
                if (!imageLink.containsKey(imageName)) {
                    // 将新和旧的图片上传到缓存，如果用户上传了图片的话，就能渲染出来
                    // 2.17:不建议直接替换，只有等用户上传了图片，再去文章中替换图片链接，
                    //  不然mavon-editor中的文章会直接访问未上传的图片链接，控制台会报很多错
                    imageLink.put(imageName, newImageName);
                }
                // 匹配成功后替换为新的图片链接
                matcher.appendReplacement(sb, parseImageLink(imageName));
            }
            matcher.appendTail(sb);
            // 将imageLink上传到缓存中
            String uploadKey = PathUtil.simpleRandomUUID();
            // 10 分钟内上传图片
            redisCache.hmset(getGenerateKey(uploadKey), imageLink, 10 * 60L);
            SysUploadArticleVO articleVO = new SysUploadArticleVO();
            articleVO.setImageCacheKey(uploadKey);
            articleVO.setImageUrl(imageLink.keySet());
            articleVO.setTitle(originalFileName);
            articleVO.setContentMd(sb.toString());
            return articleVO;
        } catch (FileNotFoundException e) {
            log.error("文件不存在");
        } catch (IOException e) {
            log.error("文件读取失败");
        }
        return null;
    }

    /**
     * 获取生成密钥
     *
     * @param key 关键
     * @return {@link String}
     */
    private String getGenerateKey(String key) {
        return CacheConstants.NEED_UPLOAD_IMAGE + key;
    }

    /**
     * 检查图像名称是否为图片格式
     *
     * @param imageName 图片名字
     * @return boolean
     */
    private boolean checkImageName(String imageName) {
        String fileType = imageName.substring(imageName.lastIndexOf(".") + 1);
        return "jpg".equals(fileType) || "png".equals(fileType);
    }

    /**
     * 得到图像名
     * 提取markdown文件中的图片的名字
     *
     * @param $2 文件路径
     * @return {@link String}
     */
    private String getImageName(String $2) {
        // 切分最后一个 "/"
        int i = $2.lastIndexOf("/");
        String fileName = $2.substring(i + 1);
        // 如果切割出来的文件还有说明 2022-12-5.jpg "测试文件上传" <-
        if (fileName.contains(" ")) {
            // 则再次切割
            return fileName.substring(0, fileName.indexOf(" "));
        } else {
            return fileName;
        }
    }

    /**
     * 解析图片链接
     * 返回![](http://domain/image)格式，以至于前端渲染时能看到图片
     *
     * @param imageName 图片名字
     * @return {@link String}
     */
    private String parseImageLink(String imageName) {
        return String.format("![](%s)", imageName);
    }


    /**
     * 一般用于增加文章的阅读量、标签点击量等等
     *
     * @param id  id
     * @param key redis中的key
     */
    public void incr(Long id, String key) {
        // 如果 hash key 存在就直接加一，否则创建一个
        redisCache.hincr(key, id.toString(), 1);
    }
}

