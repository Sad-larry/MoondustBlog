package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Dict;
import work.moonzs.domain.entity.DictData;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysDictDataVO;
import work.moonzs.mapper.DictDataMapper;
import work.moonzs.service.DictDataService;
import work.moonzs.service.DictService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典数据表(DictData)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("dictDataService")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {
    @Lazy
    @Autowired
    private DictService dictService;

    @Override
    public PageVO<SysDictDataVO> listDictData(Long dictId) {
        Page<DictData> page = new Page<>(1, 10);
        page(page, new LambdaQueryWrapper<DictData>().eq(DictData::getDictId, dictId));
        Dict dict = dictService.getById(dictId);
        List<SysDictDataVO> sysDictDataVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysDictDataVO.class)
                .stream().peek(sysDictDataVO -> sysDictDataVO.setDict(dict)).toList();
        return new PageVO<>(sysDictDataVOS, page.getTotal());
    }

    @Override
    public boolean isExistDictDataById(Long dictDataId) {
        return ObjUtil.isNotNull(getById(dictDataId));
    }

    @Override
    public Long insertDictData(DictData dictData) {
        // 若字典不存在则添加失败
        boolean isExistDict = dictService.isExistDictById(dictData.getDictId());
        BusinessAssert.isTure(isExistDict, AppHttpCodeEnum.DICT_NOT_EXIST);
        baseMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public boolean updateDictData(DictData dictData) {
        BusinessAssert.isTure(isExistDictDataById(dictData.getId()), AppHttpCodeEnum.DICT_DATA_NOT_EXIST);
        return updateById(dictData);
    }

    @Override
    public boolean deleteDictData(Long[] dictDataIds) {
        return removeBatchByIds(List.of(dictDataIds));
    }

    @Override
    public boolean deleteDictDataByDictId(Long[] dictIds) {
        LambdaUpdateWrapper<DictData> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DictData::getDictId, List.of(dictIds));
        return remove(updateWrapper);
    }

    @Override
    public Map<String, Map<String, Object>> getDataByDictType(String type) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        Long dictId = dictService.getIdByDictType(type);
        List<DictData> list = list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictId, dictId));
        String defaultDictDataValue = null;
        for (DictData dictData : list) {
            if (StatusConstants.NORMAL.equals(dictData.getIsDefault())) {
                defaultDictDataValue = dictData.getValue();
                break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("defaultValue", defaultDictDataValue);
        result.put(type, map);
        return result;
    }
}

