package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.ArticleTag;
import work.moonzs.mapper.ArticleTagMapper;
import work.moonzs.service.ArticleTagService;

import java.util.List;

/**
 * (ArticleTag)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    /**
     * 更新标签条
     *
     * @param articleId 文章id
     * @param tagList   标记列表
     */
    @Override
    public void updateArticleTags(Long articleId, List<Long> tagList) {
        // 通过文章id查询文章标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleId);
        List<ArticleTag> queryList = list(queryWrapper);
        // 如果有文章标签，说明是更新操作，否则插入新数据
        if (CollUtil.isEmpty(queryList)) {
            tagList.stream()
                    // 去重
                    .distinct().forEach(tagId -> save(new ArticleTag(null, articleId, tagId)));
            return;
        }
        // 如果是更新操作，就查询出哪些是新增的，哪些是删除的
        List<Long> collectList = queryList.stream().map(ArticleTag::getTagId).distinct().toList();
        // 如果更新的标签和数据库中的是一样的，就不执行以下操作
        if (CollUtil.containsAll(collectList, tagList) && CollUtil.containsAll(tagList, collectList)) {
            return;
        }
        // 更新的集合中有，但是数据库集合灭有，求差集 col2 - col1，值为要更新的标签id
        List<Long> saveTags = CollUtil.subtractToList(tagList, collectList);
        // 数据库集合中有，但是更新的集合没有，求差集 col1 - col2，值为要删除的标签id
        List<Long> deleteTags = CollUtil.subtractToList(collectList, tagList);
        // 更新的标签插入
        if (!CollUtil.isEmpty(saveTags)) {
            saveTags.forEach(tagId -> save(new ArticleTag(null, articleId, tagId)));
        }
        // 删除的标签删除
        if (!CollUtil.isEmpty(deleteTags)) {
            // 因为我不想用LambdaQuery查询并删除article_id=? And tag_id=?
            //  所以我就把之前查询到的queryList找到要删除的article_tag数据的id
            List<Long> deleteTagIds = queryList.stream().filter(articleTag -> CollUtil.contains(deleteTags, articleTag.getTagId())).map(ArticleTag::getId).toList();
            deleteTagIds.forEach(this::removeById);
        }
    }

    /**
     * 通过文章id删除文章关联的标签
     *
     * @param articleId 文章id
     */
    @Override
    public void deleteArticleTagById(Long articleId) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleId);
        remove(queryWrapper);
    }
}

