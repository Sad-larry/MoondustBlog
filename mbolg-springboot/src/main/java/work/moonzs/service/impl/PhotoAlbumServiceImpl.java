package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.PhotoAlbum;
import work.moonzs.mapper.PhotoAlbumMapper;
import work.moonzs.service.PhotoAlbumService;

/**
 * 相册(PhotoAlbum)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("photoAlbumService")
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {

}

