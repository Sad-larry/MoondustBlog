package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import work.moonzs.domain.dto.ArticleDTO;
import work.moonzs.domain.entity.Article;
import work.moonzs.domain.vo.ArticleVo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysArticleReadVO;
import work.moonzs.domain.vo.sys.SysUploadArticleVO;
import work.moonzs.domain.vo.web.ArticleBaseVO;
import work.moonzs.domain.vo.web.ArticleInfoVO;
import work.moonzs.domain.vo.web.ArticlePreviewVO;

import java.util.List;
import java.util.Map;

/**
 * 博客文章表(Article)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:01
 */
public interface ArticleService extends IService<Article> {

    /**
     * 发表文章
     * 继承BaseMapper的mapper类使用insert方式时，在IdType=AUTO情况下，插入的article对象自动赋予对象id
     *
     * @param articleDTO 文章dto
     * @return boolean
     */
    @Transactional
    boolean publishArticle(ArticleDTO articleDTO);

    /**
     * 文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVO}<{@link ArticleVo}>
     */
    @Transactional(readOnly = true)
    PageVO<ArticleVo> listArticle(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 通过id获取文章
     *
     * @param articleId 文章id
     * @return {@link ArticleVo}
     */
    ArticleVo getArticleById(Long articleId);

    @Transactional
    boolean updateArticle(ArticleDTO articleDTO);

    /**
     * 更新文章分类
     *
     * @param categoryName 分类名字
     * @param articleId    文章id
     * @return boolean
     */
    @Transactional
    boolean updateArticleCategory(Long articleId, String categoryName);

    /**
     * 更新标签列表
     *
     * @param articleId 文章id
     * @param tags      标签
     * @return boolean
     */
    @Transactional
    boolean updateArticleTags(Long articleId, List<String> tags);

    /**
     * 删除文章
     *
     * @param articleIds 文章ids
     * @return boolean
     */
    @Transactional
    boolean deleteArticle(Long[] articleIds);

    /**
     * 置顶文章
     *
     * @param articleId 文章id
     * @param isStick   是坚持
     * @return boolean
     */
    @Transactional
    boolean topArticle(Long articleId, Integer isStick);

    /**
     * 文章篇数
     *
     * @return {@link Long}
     */
    Long articleCount();

    /**
     * 文章列表
     *
     * @param pageNum  1
     * @param pageSize 10
     * @return {@link PageVO}<{@link ArticlePreviewVO}>
     */
    @Transactional(readOnly = true)
    PageVO<ArticlePreviewVO> listWebArticle(Integer pageNum, Integer pageSize, Long categoryId, Long tagId);

    /**
     * 获取文章详细信息
     *
     * @param articleId 文章id
     * @return {@link ArticleInfoVO}
     */
    ArticleInfoVO getArticleInfo(Long articleId);

    /**
     * 查询除该文章之外的最新文章
     *
     * @param articleId 文章id
     * @return {@link List}<{@link ArticleBaseVO}>
     */
    List<ArticleBaseVO> getNewestArticles(Long articleId);

    /**
     * 查询该文章下一篇或上一篇文章
     * type = 0, 查询上一篇文章
     * type = 1, 查询下一篇文章
     *
     * @param articleId 文章id
     * @param type      类型
     * @return {@link ArticleBaseVO}
     */
    ArticleBaseVO getNextOrLastArticle(Long articleId, Integer type);

    /**
     * 推荐文章列表
     *
     * @param articleId 文章id
     * @return {@link List}<{@link ArticleBaseVO}>
     */
    List<ArticleBaseVO> listRecommendArticle(Long articleId);

    /**
     * 查询归档
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVO}<{@link ArticleBaseVO}>
     */
    PageVO<ArticleBaseVO> getArchives(Integer pageNum, Integer pageSize);

    /**
     * 上传文章
     *
     * @param file Md文件
     * @return {@link SysUploadArticleVO}
     */
    SysUploadArticleVO uploadArticle(MultipartFile file);

    /**
     * 获取每日博客贡献数
     *
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getBlogContributeCount();

    /**
     * 获取博客阅读排行榜
     *
     * @return {@link List}<{@link SysArticleReadVO}>
     */
    List<SysArticleReadVO> getBlogReadVolume();
}

