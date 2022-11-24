package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.base.web.common.BusinessAssert;
import work.moonzs.domain.entity.PhotoAlbum;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysPhotoAlbumVO;
import work.moonzs.mapper.PhotoAlbumMapper;
import work.moonzs.service.PhotoAlbumService;
import work.moonzs.service.PhotoService;

import java.util.List;

/**
 * 相册(PhotoAlbum)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("photoAlbumService")
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {
    @Autowired
    private PhotoService photoService;

    @Override
    public boolean isExistPhotoAlbumById(Long photoAlbumId) {
        return ObjUtil.isNotNull(getById(photoAlbumId));
    }

    @Override
    public PageVO<SysPhotoAlbumVO> listPhotoAlbum(Integer pageNum, Integer pageSize, String name) {
        Page<PhotoAlbum> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(name)) {
            page(page, new LambdaQueryWrapper<PhotoAlbum>().like(PhotoAlbum::getName, name));
        } else {
            page(page);
        }
        List<SysPhotoAlbumVO> sysPhotoAlbumVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysPhotoAlbumVO.class);
        sysPhotoAlbumVOS.forEach(sysPhotoAlbumVO -> {
            sysPhotoAlbumVO.setPhotoCount(photoService.photoCountByAlbumId(sysPhotoAlbumVO.getId()));
        });
        return new PageVO<>(sysPhotoAlbumVOS, page.getTotal());
    }

    @Override
    public SysPhotoAlbumVO getPhotoAlbumById(Long photoAlbumId) {
        PhotoAlbum byId = getById(photoAlbumId);
        SysPhotoAlbumVO sysPhotoAlbumVO = null;
        if (ObjUtil.isNotNull(byId)) {
            sysPhotoAlbumVO = BeanCopyUtil.copyBean(byId, SysPhotoAlbumVO.class);
            sysPhotoAlbumVO.setPhotoCount(photoService.photoCountByAlbumId(sysPhotoAlbumVO.getId()));
        }
        return sysPhotoAlbumVO;
    }

    @Override
    public Long insertPhotoAlbum(PhotoAlbum photoAlbum) {
        baseMapper.insert(photoAlbum);
        return photoAlbum.getId();
    }

    @Override
    public boolean updatePhotoAlbum(PhotoAlbum photoAlbum) {
        BusinessAssert.isTure(isExistPhotoAlbumById(photoAlbum.getId()), AppHttpCodeEnum.DATA_NOT_EXIST);
        return updateById(photoAlbum);
    }

    @Override
    public boolean deletePhotoAlbum(Long photoAlbumIds) {
        // 删除该相册里的所有照片
        photoService.deletePhotoByAlbumId(photoAlbumIds);
        return removeById(photoAlbumIds);
    }
}

