package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.TagVo;
import work.moonzs.domain.vo.web.TagVO;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.TagService;

import java.util.List;

/**
 * 博客标签表(Tag)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:27
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public boolean isExistTagByIds(List<Long> tagIds) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, tagIds);
        // 根据字段判断数据库数否存在数据不应该有其他字段吧
        return count(queryWrapper) == tagIds.size();
    }

    @Override
    public boolean isExistTagByTagName(String tagName) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tagName);
        // 不同判断状态
        return count(queryWrapper) > 0;
    }

    /**
     * 判断是否存在相同标签
     *
     * @param tagName 标签名
     * @return boolean
     */
    private boolean isExistSameTagByTagName(String tagName) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tagName);
        // 不同判断状态
        return count(queryWrapper) > 1;
    }

    @Override
    public PageVO<TagVo> listTag(Integer pageNum, Integer pageSize, String fuzzyField) {
        Page<Tag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        queryWrapper.like(StrUtil.isNotBlank(fuzzyField), Tag::getName, fuzzyField);
        page(page, queryWrapper);
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), TagVo.class), page.getTotal());
    }

    @Override
    public TagVo getTagById(Long tagId) {
        Tag byId = getById(tagId);
        return ObjUtil.isNotNull(byId) ? BeanCopyUtil.copyBean(byId, TagVo.class) : null;
    }

    @Override
    public boolean insertTag(Tag tag) {
        // 判断标签名是否有相同的，有就不添加
        BusinessAssert.isFalse(isExistTagByTagName(tag.getName()), AppHttpCodeEnum.TAG_EXIST);
        return save(tag);
    }

    @Override
    public boolean updateTag(Tag tag) {
        // 判断该id标签是否存在
        BusinessAssert.isTure(isExistTagByIds(List.of(tag.getId())), AppHttpCodeEnum.TAG_NOT_EXIST);
        updateById(tag);
        // 更新之后判断是否存在重复标签
        BusinessAssert.isFalse(isExistSameTagByTagName(tag.getName()), AppHttpCodeEnum.TAG_EXIST);
        return true;
    }

    @Override
    public boolean deleteTag(Long[] tagIds) {
        return removeBatchByIds(List.of(tagIds));
    }

    @Override
    public String getTagStrByArticleId(Long articleId) {
        return getBaseMapper().getTagsByArticleId(articleId);
    }

    @Override
    public List<TagVO> listWebTag() {
        List<Tag> list = list(new LambdaQueryWrapper<Tag>().select(Tag::getId, Tag::getName));
        return BeanCopyUtil.copyBeanList(list, TagVO.class);
    }

    @Override
    public List<TagVO> getBlogTagsByArticleId(Long id) {
        return baseMapper.selectByArticleId(id);
    }
}

