package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.PhotoDTO;
import work.moonzs.domain.entity.Photo;
import work.moonzs.service.PhotoService;

/**
 * @author Moondust月尘
 */
@RestController("SystemPhotoC")
@RequestMapping("/system/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    /**
     * 图片列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param albumId  相册id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取照片列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:photo:list')")
    public ResponseResult listPhoto(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam Long albumId) {
        return ResponseResult.successPageVO(photoService.listPhoto(pageNum, pageSize, albumId));
    }

    /**
     * 通过id查询照片详细信息
     *
     * @param photoId 照片id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询照片详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('system:photo:info')")
    public ResponseResult getPhotoById(@PathVariable(value = "id") Long photoId) {
        return ResponseResult.success(photoService.getPhotoById(photoId));
    }

    /**
     * 添加照片
     *
     * @param photoDTO 照片dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加照片")
    @AdminOperationLogger(value = "添加照片")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:photo:add')")
    public ResponseResult addPhoto(@Validated(VG.Insert.class) @RequestBody PhotoDTO photoDTO) {
        photoService.insertPhoto(BeanCopyUtil.copyBean(photoDTO, Photo.class));
        return ResponseResult.success();
    }


    /**
     * 更新照片
     *
     * @param photoDTO 照片dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新照片")
    @AdminOperationLogger(value = "更新照片")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:photo:update')")
    public ResponseResult updatePhoto(@Validated(VG.Update.class) @RequestBody PhotoDTO photoDTO) {
        photoService.updatePhoto(BeanCopyUtil.copyBean(photoDTO, Photo.class));
        return ResponseResult.success();
    }

    /**
     * 删除照片
     *
     * @param photoIds 照片id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据照片id进行批量删除操作")
    @AdminOperationLogger(value = "删除照片")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('system:photo:delete')")
    public ResponseResult deletePhoto(@PathVariable(value = "ids") Long[] photoIds) {
        photoService.deletePhoto(photoIds);
        return ResponseResult.success();
    }

    /**
     * 移动照片
     *
     * @param albumId 相册id
     * @param ids     id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "移动照片")
    @AdminOperationLogger(value = "移动照片")
    @GetMapping(value = "/movePhoto")
    @PreAuthorize("@ss.hasPermi('system:photo:movePhoto')")
    public ResponseResult movePhoto(@RequestParam Long albumId, @RequestParam Long[] ids) {
        photoService.movePhoto(albumId, ids);
        return ResponseResult.success();
    }
}
