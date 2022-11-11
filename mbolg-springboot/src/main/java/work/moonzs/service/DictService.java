package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Dict;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysDictVO;

/**
 * 字典表(Dict)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface DictService extends IService<Dict> {
    /**
     * 字典列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param name     字典名
     * @return {@link PageVO}<{@link SysDictVO}>
     */
    PageVO<SysDictVO> listDict(Integer pageNum, Integer pageSize, String name);

    /**
     * 通过字典id判断是否存在
     *
     * @param dictId 字典id
     * @return boolean
     */
    boolean isExistDictById(Long dictId);

    /**
     * 通过字典类型判断是存在字典
     *
     * @param type 类型
     * @return boolean
     */
    boolean isExistDictByType(String type);

    /**
     * 通过字典类型判断是存在相同字典
     *
     * @param type 类型
     * @return boolean
     */
    boolean isExistSameDictByType(String type);

    /**
     * 插入字典
     *
     * @param dict 字典
     * @return {@link Long}
     */
    @Transactional
    Long insertDict(Dict dict);

    /**
     * 更新字典
     *
     * @param dict 字典
     * @return boolean
     */
    @Transactional
    boolean updateDict(Dict dict);

    /**
     * 删除字典
     *
     * @param dictIds dict id
     * @return boolean
     */
    @Transactional
    boolean deleteDict(Long[] dictIds);

    /**
     * 通过字典类型获取字典ID
     *
     * @param type 类型
     * @return {@link Long}
     */
    Long getIdByDictType(String type);
}

