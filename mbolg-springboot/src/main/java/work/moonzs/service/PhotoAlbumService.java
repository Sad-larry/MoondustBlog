package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.PhotoAlbum;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysPhotoAlbumVO;

/**
 * 相册(PhotoAlbum)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:45
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {
    /**
     * 通过id判断相册是否存在
     *
     * @param photoAlbumId 相册id
     * @return boolean
     */
    boolean isExistPhotoAlbumById(Long photoAlbumId);

    /**
     * 相册列表
     *
     * @param name     相册名字
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVO}<{@link SysPhotoAlbumVO}>
     */
    PageVO<SysPhotoAlbumVO> listPhotoAlbum(Integer pageNum, Integer pageSize, String name);

    /**
     * 获取相册id
     *
     * @param photoAlbumId 相册id
     * @return {@link SysPhotoAlbumVO}
     */
    SysPhotoAlbumVO getPhotoAlbumById(Long photoAlbumId);

    /**
     * 插入相册
     *
     * @param photoAlbum 相册
     * @return {@link Long}
     */
    @Transactional
    Long insertPhotoAlbum(PhotoAlbum photoAlbum);

    /**
     * 更新相册
     *
     * @param photoAlbum 相册
     * @return boolean
     */
    @Transactional
    boolean updatePhotoAlbum(PhotoAlbum photoAlbum);

    /**
     * 删除相册
     * 删除相册时会删除其内照片
     *
     * @param photoAlbumIds 相册id
     * @return boolean
     */
    @Transactional
    boolean deletePhotoAlbum(Long photoAlbumIds);
}

