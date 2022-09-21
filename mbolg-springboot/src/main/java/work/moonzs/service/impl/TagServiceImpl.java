package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Tag;
import work.moonzs.mapper.TagMapper;
import work.moonzs.service.TagService;

/**
 * (Tag)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}

