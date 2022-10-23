package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Tag;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.TagVo;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.TagService;

import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public boolean isExistTagByIds(List<Long> tagIds) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, tagIds);
        // 根据字段判断数据库数否存在数据不应该有其他字段吧，特别是status
        // 但是你判断存在数据存在肯定是要根据状态正常的数据判断的吧
        queryWrapper.eq(Tag::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count == tagIds.size();
    }

    @Override
    public boolean isExistTagByTagName(String tagName) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getTagName, tagName);
        // 不同判断状态
        long count = count(queryWrapper);
        return count > 0;
    }

    @Override
    public PageVo<TagVo> listTags(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊字段为空则不匹配
        if (!StrUtil.isBlank(fuzzyField)) {
            queryWrapper.like(Tag::getTagName, fuzzyField);
            queryWrapper.or().like(Tag::getDescription, fuzzyField);
        }
        queryWrapper.eq(Tag::getStatus, StatusConstants.NORMAL);
        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Tag> list = page.getRecords();
        List<TagVo> tagListVos = BeanCopyUtil.copyBeanList(list, TagVo.class);
        return new PageVo<>(tagListVos, page.getTotal(), page.getCurrent(), page.getSize());
    }
}

