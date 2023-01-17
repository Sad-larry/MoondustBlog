package work.moonzs.controller.system;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.DictDataDTO;
import work.moonzs.domain.entity.DictData;
import work.moonzs.service.DictDataService;

/**
 * @author Moondust月尘
 */
@RestController("SystemDictDataC")
@RequestMapping("/system/dict/data")
public class DictDataController {
    @Autowired
    private DictDataService dictDataService;

    /**
     * 字典数据列表
     *
     * @param dictId 字典ID
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "字典数据列表")
    @GetMapping("/list")
    public ResponseResult listDictData(@RequestParam("dictId") Long dictId) {
        return ResponseResult.success(dictDataService.listDictData(dictId));
    }

    /**
     * 添加字典数据
     *
     * @param dictDTO 字典数据dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加字典数据")
    @AdminOperationLogger(value = "添加字典数据")
    @PostMapping
    public ResponseResult addDictData(@Validated(VG.Insert.class) @RequestBody DictDataDTO dictDTO) {
        dictDataService.insertDictData(BeanCopyUtil.copyBean(dictDTO, DictData.class));
        return ResponseResult.success();
    }

    /**
     * 更新字典数据
     *
     * @param dictDTO 字典数据dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新字典数据")
    @AdminOperationLogger(value = "更新字典数据")
    @PutMapping
    public ResponseResult updateDictData(@Validated(VG.Update.class) @RequestBody DictDataDTO dictDTO) {
        dictDataService.updateDictData(BeanCopyUtil.copyBean(dictDTO, DictData.class));
        return ResponseResult.success();
    }

    /**
     * 删除字典数据
     *
     * @param dictIds 字典数据id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据字典数据id进行批量删除操作")
    @AdminOperationLogger(value = "删除字典数据")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteDictData(@PathVariable(value = "ids") Long[] dictIds) {
        dictDataService.deleteDictData(dictIds);
        return ResponseResult.success();
    }

    /**
     * 根据字典类型获取字典数据
     *
     * @param types 类型
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据字典类型获取字典数据")
    @PostMapping(value = "/getByType")
    public ResponseResult getDataByDictType(@RequestBody String[] types) {
        return ResponseResult.success(dictDataService.getDataByDictType(types));
    }
}
