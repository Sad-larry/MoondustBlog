package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.moonzs.domain.entity.Tag;

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
    boolean isExistTagById(List<Long> tagIds);
}

