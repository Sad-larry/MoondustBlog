package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.Page;
import work.moonzs.mapper.PageMapper;
import work.moonzs.service.PageService;

/**
 * 前端页面表(Page)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("pageService")
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

}

