package work.moonzs.controller.system;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.TagDTO;
import work.moonzs.domain.entity.Tag;
import work.moonzs.service.TagService;

import java.util.List;

/**
 * @author Moondust月尘
 */
@RestController("SystemTagC")
@RequestMapping("/system/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 添加标签
     *
     * @param tagDTO 标签dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PostMapping
    public ResponseResult<?> addTag(@RequestBody TagDTO tagDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        if (StrUtil.isBlank(tagDTO.getTagName()) || StrUtil.isBlank(tagDTO.getDescription())) {
            return ResponseResult.fail(AppHttpCodeEnum.FIELD_EMPTY);
        }
        // 判断标签名是否有相同的，有就不添加
        boolean isExistTag = tagService.isExistTagByTagName(tagDTO.getTagName());
        if (isExistTag) {
            // TODO 新增的话，如果新增的标签跟删除的标签是一样的话就将删除的标签状态设置为1
            return ResponseResult.fail(AppHttpCodeEnum.TAG_EXIST);
        }
        tagDTO.setId(null);
        Tag tag = BeanCopyUtil.copyBean(tagDTO, Tag.class);
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


    /**
     * 更新标签
     *
     * @param tagDTO 标签dto
     * @return {@link ResponseResult}<{@link ?}>
     */
    @PutMapping
    public ResponseResult<?> updateTag(@RequestBody TagDTO tagDTO) {
        // TODO 这里应该用字段校验，我先暂时手动检验
        // tagName，description，status不为空
        if (StrUtil.isBlank(tagDTO.getTagName()) || StrUtil.isBlank(tagDTO.getDescription()) || StrUtil.isBlank(tagDTO.getStatus())) {
            return ResponseResult.fail(AppHttpCodeEnum.FIELD_EMPTY);
        }
        // 判断该id标签是否存在
        boolean isExistTagId = tagService.isExistTagByIds(List.of(tagDTO.getId()));
        if (!isExistTagId) {
            return ResponseResult.fail(AppHttpCodeEnum.TAG_NOT_EXIST);
        }
        // 判断是否存在相同标签名，标签描述可以该阿，标签状态可以改阿，你这么写，就只判断标签名，其他的不能改了是吧（旧版）
        Tag byId = tagService.getById(tagDTO.getId());
        Tag tag = BeanCopyUtil.copyBean(tagDTO, Tag.class);
        // 判断byId和tag中的tagName，description，status是否相等
        if (tag.equals(byId)) {
            return ResponseResult.fail(AppHttpCodeEnum.TAG_EXIST);
        }
        tagService.updateById(tag);
        return ResponseResult.success();
    }

    /**
     * 如果硬要删除标签的话，不应该吧，修改状态不香吗
     *
     * @param tagId 标签id
     * @return {@link ResponseResult}<{@link ?}>
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteTag(@PathVariable(value = "id") Long tagId) {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setStatus(StatusConstants.DISABLE);
        tagService.updateById(tag);
        return ResponseResult.success();
    }
}
