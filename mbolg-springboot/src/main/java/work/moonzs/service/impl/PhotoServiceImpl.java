package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.Photo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysPhotoVO;
import work.moonzs.mapper.PhotoMapper;
import work.moonzs.service.PhotoAlbumService;
import work.moonzs.service.PhotoService;

import java.util.List;

/**
 * 照片(Photo)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("photoService")
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {
    @Autowired
    @Lazy
    private PhotoAlbumService photoAlbumService;

    /**
     * 通过id判断照片是否存在
     *
     * @param photoId 照片id
     * @return boolean
     */
    private boolean isExistPhotoById(Long photoId) {
        return ObjUtil.isNotNull(getById(photoId));
    }

    /**
     * 判断相册是否存在并处理
     *
     * @param albumId 相册id
     */
    private void isExistAblumById(Long albumId) {
        // 查询相册是否存在
        boolean isExistPhotoAlbum = photoAlbumService.isExistPhotoAlbumById(albumId);
        BusinessAssert.isTure(isExistPhotoAlbum, AppHttpCodeEnum.DATA_NOT_EXIST);
    }

    @Override
    public PageVO<SysPhotoVO> listPhoto(Integer pageNum, Integer pageSize, Long albumId) {
        Page<Photo> page = new Page<>(pageNum, pageSize);
        page(page, new LambdaQueryWrapper<Photo>().eq(Photo::getAlbumId, albumId));
        return new PageVO<>(BeanCopyUtil.copyBeanList(page.getRecords(), SysPhotoVO.class), page.getTotal());
    }

    @Override
    public SysPhotoVO getPhotoById(Long photoId) {
        Photo byId = getById(photoId);
        return ObjUtil.isNotNull(byId) ?
                BeanCopyUtil.copyBean(byId, SysPhotoVO.class) : null;
    }

    @Override
    public Long insertPhoto(Photo photo) {
        // 判断相册是否存在
        isExistAblumById(photo.getAlbumId());
        baseMapper.insert(photo);
        return photo.getId();
    }

    @Override
    public boolean updatePhoto(Photo photo) {
        isExistAblumById(photo.getAlbumId());
        return updateById(photo);
    }

    @Override
    public boolean deletePhoto(Long[] photoIds) {
        return removeBatchByIds(List.of(photoIds));
    }

    @Override
    public boolean movePhoto(Long albumId, Long[] photoIds) {
        isExistAblumById(albumId);
        LambdaUpdateWrapper<Photo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Photo::getId, List.of(photoIds)).set(Photo::getAlbumId, albumId);
        return update(updateWrapper);
    }

    @Override
    public boolean deletePhotoByAlbumId(Long photoAlbumId) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, photoAlbumId);
        return remove(queryWrapper);
    }

    @Override
    public Long photoCountByAlbumId(Long photoAlbumId) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, photoAlbumId);
        return count(queryWrapper);
    }
}
