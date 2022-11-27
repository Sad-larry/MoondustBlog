package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.DictData;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysDictDataVO;

import java.util.Map;

/**
 * 字典数据数据表(DictData)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface DictDataService extends IService<DictData> {
    /**
     * dict列表数据
     * 字典数据列表
     *
     * @param dictId dict id
     * @return {@link PageVO}<{@link SysDictDataVO}>
     */
    PageVO<SysDictDataVO> listDictData(Long dictId);

    /**
     * 通过字典数据id判断是否存在
     *
     * @param dictDataId 字典数据id
     * @return boolean
     */
    boolean isExistDictDataById(Long dictDataId);

    /**
     * 插入字典数据
     *
     * @param dictData 字典数据
     * @return {@link Long}
     */
    @Transactional
    Long insertDictData(DictData dictData);

    /**
     * 更新字典数据
     *
     * @param dictData 字典数据
     * @return boolean
     */
    @Transactional
    boolean updateDictData(DictData dictData);

    /**
     * 删除字典数据
     *
     * @param dictDataIds dict id
     * @return boolean
     */
    @Transactional
    boolean deleteDictData(Long[] dictDataIds);

    /**
     * 通过字典id删除字典数据
     *
     * @param dictIds dict id
     * @return boolean
     */
    @Transactional
    boolean deleteDictDataByDictId(Long[] dictIds);

    /**
     * 根据字典类型获取字典数据
     *
     * @param types 类型
     * @return {@link Map}<{@link String}, {@link Map}<{@link String}, {@link Object}>>
     */
    Map<String, Map<String, Object>> getDataByDictType(String[] types);
}

