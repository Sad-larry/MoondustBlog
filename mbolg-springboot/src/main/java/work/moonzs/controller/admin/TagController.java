package work.moonzs.controller.admin;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.TagDTO;
import work.moonzs.domain.entity.Tag;
import work.moonzs.enums.AppHttpCodeEnum;
import work.moonzs.service.TagService;
import work.moonzs.utils.BeanCopyUtils;

/**
 * @author Moondust月尘
 */
@RestController(value = "AdminTagC")
@RequestMapping("/admin/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseResult<?> addTag(@RequestBody TagDTO tagDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        if (StrUtil.isBlank(tagDTO.getTagName()) && StrUtil.isBlank(tagDTO.getDescription())) {
            return ResponseResult.fail();
        }
        // 判断标签名是否有相同的，有就不添加
        boolean isExistTag = tagService.isExistTagByTagName(tagDTO.getTagName());
        if (isExistTag) {
            return ResponseResult.fail(AppHttpCodeEnum.TAG_EXIST);
        }
        tagDTO.setId(null);
        Tag tag = BeanCopyUtils.copyBean(tagDTO, Tag.class);
        tagService.save(tag);
        return ResponseResult.success();
    }


    /**
     * 列表标签
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    @GetMapping("/list")
    public ResponseResult<?> listTags(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String fuzzyField) {
        return tagService.listTags(pageNum, pageSize, fuzzyField);
    }


    @PutMapping
    public ResponseResult<?> updateTag(@RequestBody TagDTO tagDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        if (StrUtil.isBlank(tagDTO.getTagName()) && StrUtil.isBlank(tagDTO.getDescription())) {
            return ResponseResult.fail();
        }
        // 判断标签名是否有相同的，有就不添加
        // boolean isExistTag = tagService.isExistTagByTagName(tagDTO.getTagName());
        // if (isExistTag) {
        //     return ResponseResult.fail(AppHttpCodeEnum.TAG_EXIST);
        // }
        tagDTO.setId(null);
        Tag tag = BeanCopyUtils.copyBean(tagDTO, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.success();
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteTag(@PathVariable(value = "id") Long tagId) {

        return ResponseResult.success();
    }
}
