package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.TagVo;

import java.util.List;

/**
 * (Tag)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
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
    PageVo<TagVo> listTags(Integer pageNum, Integer pageSize, String fuzzyField);
}

