package work.moonzs.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.moonzs.base.annotation.AdminOperationLogger;
import work.moonzs.base.annotation.SystemLog;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.validate.VG;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.dto.PhotoAlbumDTO;
import work.moonzs.domain.entity.PhotoAlbum;
import work.moonzs.service.PhotoAlbumService;

/**
 * @author Moondust月尘
 */
@RestController("SystemPhotoAlbumC")
@RequestMapping("/system/album")
@RequiredArgsConstructor
public class PhotoAlbumController {
    private final PhotoAlbumService photoAlbumService;

    /**
     * 相册列表
     *
     * @param name     相册名字
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "获取相册列表")
    @GetMapping("/list")
    public ResponseResult listPhotoAlbum(@RequestParam(defaultValue = "1", required = false) Integer pageNum, @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "", required = false) String name) {
        return ResponseResult.successPageVO(photoAlbumService.listPhotoAlbum(pageNum, pageSize, name));
    }

    /**
     * 通过id查询相册详细信息
     *
     * @param photoAlbumId 相册id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "通过id查询相册详细信息")
    @GetMapping("/{id}")
    public ResponseResult getPhotoAlbumById(@PathVariable(value = "id") Long photoAlbumId) {
        return ResponseResult.success(photoAlbumService.getPhotoAlbumById(photoAlbumId));
    }

    /**
     * 添加相册
     *
     * @param photoAlbumDTO 相册dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "添加相册")
    @AdminOperationLogger(value = "添加相册")
    @PostMapping
    public ResponseResult addPhotoAlbum(@Validated(VG.Insert.class) @RequestBody PhotoAlbumDTO photoAlbumDTO) {
        photoAlbumService.insertPhotoAlbum(BeanCopyUtil.copyBean(photoAlbumDTO, PhotoAlbum.class));
        return ResponseResult.success();
    }


    /**
     * 更新相册
     *
     * @param photoAlbumDTO 相册dto
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "更新相册")
    @AdminOperationLogger(value = "更新相册")
    @PutMapping
    public ResponseResult updatePhotoAlbum(@Validated(VG.Update.class) @RequestBody PhotoAlbumDTO photoAlbumDTO) {
        photoAlbumService.updatePhotoAlbum(BeanCopyUtil.copyBean(photoAlbumDTO, PhotoAlbum.class));
        return ResponseResult.success();
    }

    /**
     * 删除相册
     *
     * @param photoAlbumId 相册id
     * @return {@link ResponseResult}
     */
    @SystemLog(businessName = "根据相册id进行批量删除操作")
    @AdminOperationLogger(value = "删除相册")
    @DeleteMapping("/{id}")
    public ResponseResult deletePhotoAlbum(@PathVariable(value = "id") Long photoAlbumId) {
        photoAlbumService.deletePhotoAlbum(photoAlbumId);
        return ResponseResult.success();
    }
}
