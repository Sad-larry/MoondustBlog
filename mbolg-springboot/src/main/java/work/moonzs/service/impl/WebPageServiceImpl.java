package work.moonzs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.WebPage;
import work.moonzs.domain.vo.sys.SysWebPageVO;
import work.moonzs.mapper.WebPageMapper;
import work.moonzs.service.WebPageService;

import java.util.List;

/**
 * 前端页面表(WebPage)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
@Service("webPageService")
public class WebPageServiceImpl extends ServiceImpl<WebPageMapper, WebPage> implements WebPageService {

    @Override
    public List<SysWebPageVO> listWebPage() {
        List<WebPage> list = list();
        return CollUtil.isNotEmpty(list) ? BeanCopyUtil.copyBeanList(list, SysWebPageVO.class) : null;
    }


    @Override
    public SysWebPageVO insertWebPage(WebPage webPage) {
        baseMapper.insert(webPage);
        return BeanCopyUtil.copyBean(webPage, SysWebPageVO.class);
    }

    @Override
    public boolean updateWebPage(WebPage webPage) {
        return updateById(webPage);
    }

    @Override
    public boolean deleteWebPage(Long webPageId) {
        return removeById(webPageId);
    }

    @Override
    public List<SysWebPageVO> getPageList() {
        LambdaQueryWrapper<WebPage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(WebPage::getPageName, WebPage::getPageLabel, WebPage::getPageCover);
        List<WebPage> list = list(queryWrapper);
        return CollUtil.isNotEmpty(list) ? BeanCopyUtil.copyBeanList(list, SysWebPageVO.class) : null;
    }
}

