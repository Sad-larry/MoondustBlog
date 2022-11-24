package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Photo;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysPhotoVO;

/**
 * 照片(Photo)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:45
 */
public interface PhotoService extends IService<Photo> {
    /**
     * 图片列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param albumId  相册id
     * @return {@link PageVO}<{@link SysPhotoVO}>
     */
    PageVO<SysPhotoVO> listPhoto(Integer pageNum, Integer pageSize, Long albumId);

    /**
     * 获取照片id
     *
     * @param photoId 照片id
     * @return {@link SysPhotoVO}
     */
    SysPhotoVO getPhotoById(Long photoId);

    /**
     * 插入照片
     *
     * @param photo 照片
     * @return {@link Long}
     */
    @Transactional
    Long insertPhoto(Photo photo);

    /**
     * 更新照片
     *
     * @param photo 照片
     * @return boolean
     */
    @Transactional
    boolean updatePhoto(Photo photo);

    /**
     * 删除照片
     *
     * @param photoIds 照片id
     * @return boolean
     */
    @Transactional
    boolean deletePhoto(Long[] photoIds);

    /**
     * 移动照片
     *
     * @param albumId  相册id
     * @param photoIds 照片ids
     */
    @Transactional
    boolean movePhoto(Long albumId, Long[] photoIds);

    /**
     * 删除相册下的所有照片
     *
     * @param photoAlbumId 相册id
     * @return boolean
     */
    @Transactional
    boolean deletePhotoByAlbumId(Long photoAlbumId);

    /**
     * 查找相册里的照片数
     *
     * @param photoAlbumId 相册id
     * @return {@link Long}
     */
    Long photoCountByAlbumId(Long photoAlbumId);
}

