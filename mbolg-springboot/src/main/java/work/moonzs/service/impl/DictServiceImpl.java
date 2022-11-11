package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Dict;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysDictVO;
import work.moonzs.mapper.DictMapper;
import work.moonzs.service.DictDataService;
import work.moonzs.service.DictService;

import java.util.List;

/**
 * 字典表(Dict)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Lazy
    @Autowired
    private DictDataService dictDataService;

    @Override
    public PageVO<SysDictVO> listDict(Integer pageNum, Integer pageSize, String name) {
        Page<Dict> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(name)) {
            page(page, new LambdaQueryWrapper<Dict>().like(Dict::getName, name));
        } else {
            page(page);
        }
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysDictVO.class), page);
    }

    @Override
    public boolean isExistDictById(Long dictId) {
        return ObjUtil.isNotNull(getById(dictId));
    }

    @Override
    public boolean isExistDictByType(String type) {
        return count(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type)) > 0;
    }

    @Override
    public boolean isExistSameDictByType(String type) {
        return count(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type)) > 1;
    }

    @Override
    public Long insertDict(Dict dict) {
        BusinessAssert.isFalse(isExistDictByType(dict.getType()), AppHttpCodeEnum.DICT_EXIST);
        baseMapper.insert(dict);
        return dict.getId();
    }

    @Override
    public boolean updateDict(Dict dict) {
        BusinessAssert.isTure(isExistDictById(dict.getId()), AppHttpCodeEnum.DICT_NOT_EXIST);
        updateById(dict);
        BusinessAssert.isFalse(isExistSameDictByType(dict.getType()), AppHttpCodeEnum.DICT_EXIST);
        return true;
    }

    @Override
    public boolean deleteDict(Long[] dictIds) {
        dictDataService.deleteDictDataByDictId(dictIds);
        return removeBatchByIds(List.of(dictIds));
    }

    @Override
    public Long getIdByDictType(String type) {
        Dict dict = getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getType, type));
        // 字典不存在则返回失败
        BusinessAssert.notNull(dict, AppHttpCodeEnum.DICT_NOT_EXIST);
        return dict.getId();
    }
}