package work.moonzs.controller.system;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.DictDTO;
import work.moonzs.domain.entity.Dict;
import work.moonzs.service.DictService;

/**
 * @author Moondust月尘
 */
@RestController("SystemDictC")
@RequestMapping("/system/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 字典列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "字典列表")
    @GetMapping("/list")
    public ResponseResult listDict(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String name) {
        return ResponseResult.success(dictService.listDict(pageNum, pageSize, name));
    }

    /**
     * 添加字典
     *
     * @param dictDTO 字典dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加字典")
    @AdminOperationLogger(value = "添加字典")
    @PostMapping
    public ResponseResult addDict(@Validated(VG.Insert.class) @RequestBody DictDTO dictDTO) {
        dictService.insertDict(BeanCopyUtil.copyBean(dictDTO, Dict.class));
        return ResponseResult.success();
    }

    /**
     * 更新字典
     *
     * @param dictDTO 字典dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新字典")
    @AdminOperationLogger(value = "更新字典")
    @PutMapping
    public ResponseResult updateDict(@Validated(VG.Update.class) @RequestBody DictDTO dictDTO) {
        dictService.updateDict(BeanCopyUtil.copyBean(dictDTO, Dict.class));
        return ResponseResult.success();
    }

    /**
     * 删除字典
     *
     * @param dictIds 字典id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据字典id进行批量删除操作")
    @AdminOperationLogger(value = "删除字典")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteDict(@PathVariable(value = "ids") Long[] dictIds) {
        dictService.deleteDict(dictIds);
        return ResponseResult.success();
    }
}
