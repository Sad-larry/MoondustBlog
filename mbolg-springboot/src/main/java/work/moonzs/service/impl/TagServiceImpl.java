package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Tag;
import work.moonzs.enums.StatusConstants;
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

    /**
     * 通过标签id判断是否存在于数据库
     *
     * @param tagIds 标签id
     * @return boolean
     */
    @Override
    public boolean isExistTagById(List<Long> tagIds) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, tagIds);
        queryWrapper.eq(Tag::getStatus, StatusConstants.NORMAL);
        long count = count(queryWrapper);
        return count == tagIds.size();
    }
}

