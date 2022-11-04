package work.moonzs.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.TagDTO;
import work.moonzs.domain.entity.Tag;
import work.moonzs.service.TagService;

/**
 * @author Moondust月尘
 */
@RestController("SystemTagC")
@RequestMapping("/system/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 列表标签
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取标签列表")
    @GetMapping("/list")
    public ResponseResult listTag(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return ResponseResult.success(tagService.listTag(pageNum, pageSize, fuzzyField));
    }

    /**
     * 通过id获取标签
     *
     * @param tagId 标签id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询标签详细信息")
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable(value = "id") Long tagId) {
        return ResponseResult.success(tagService.getTagById(tagId));
    }

    /**
     * 添加标签
     *
     * @param tagDTO 标签dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加标签")
    @PostMapping
    public ResponseResult addTag(@Validated(VG.Insert.class) @RequestBody TagDTO tagDTO) {
        tagService.insertTag(BeanCopyUtil.copyBean(tagDTO, Tag.class));
        return ResponseResult.success();
    }

    /**
     * 更新标签
     *
     * @param tagDTO 标签dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新标签")
    @PutMapping
    public ResponseResult updateTag(@Validated(VG.Update.class) @RequestBody TagDTO tagDTO) {
        tagService.updateTag(BeanCopyUtil.copyBean(tagDTO, Tag.class));
        return ResponseResult.success();
    }

    /**
     * 删除标签
     *
     * @param tagIds 标签id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据标签id进行批量删除操作")
    @DeleteMapping("/{ids}")
    public ResponseResult deleteTag(@PathVariable(value = "ids") Long[] tagIds) {
        tagService.deleteTag(tagIds);
        return ResponseResult.success();
    }
}
