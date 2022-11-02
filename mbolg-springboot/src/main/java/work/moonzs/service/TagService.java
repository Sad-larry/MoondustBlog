package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.TagVo;

import java.util.List;

/**
 * 博客标签表(Tag)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:53
 */
public interface TagService extends IService<Tag> {
    /**
     * 通过标签id判断是否存在于数据库
     *
     * @param tagIds 标签id
     * @return boolean
     */
    boolean isExistTagByIds(List<Long> tagIds);

    /**
     * 通过标签名字判断是否存在相同标签
     *
     * @param tagName 标签名
     * @return boolean
     */
    boolean isExistTagByTagName(String tagName);

    /**
     * 列表标签
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVo}<{@link TagVo}>
     */
    PageVo<TagVo> listTag(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 通过id获取标签
     *
     * @param tagId 标签id
     * @return {@link TagVo}
     */
    TagVo getTagById(Long tagId);

    /**
     * 插入标签
     *
     * @param tag 标签
     * @return boolean
     */
    @Transactional
    boolean insertTag(Tag tag);

    /**
     * 更新标签
     *
     * @param tag 标签
     * @return boolean
     */
    @Transactional
    boolean updateTag(Tag tag);

    /**
     * 删除标签
     *
     * @param tagIds 标签id
     * @return boolean
     */
    @Transactional
    boolean deleteTag(Long[] tagIds);
}

