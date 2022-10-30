package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.DictData;
import work.moonzs.mapper.DictDataMapper;
import work.moonzs.service.DictDataService;

/**
 * 字典数据表(DictData)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("dictDataService")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

}

