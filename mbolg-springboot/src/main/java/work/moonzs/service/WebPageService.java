package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.WebPage;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysWebPageVO;

import java.util.List;

/**
 * 前端页面表(WebPage)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:17:48
 */
public interface WebPageService extends IService<WebPage> {

    /**
     * web页面列表
     *
     * @return {@link PageVO}<{@link SysWebPageVO}>
     */
    List<SysWebPageVO> listWebPage();

    /**
     * 插入web页面
     *
     * @param webPage 网页
     * @return {@link Long}
     */
    @Transactional
    SysWebPageVO insertWebPage(WebPage webPage);

    /**
     * 更新网页
     *
     * @param webPage 网页
     * @return boolean
     */
    @Transactional
    boolean updateWebPage(WebPage webPage);

    /**
     * 删除网页
     *
     * @param webPageId 网页id
     * @return boolean
     */
    @Transactional
    boolean deleteWebPage(Long webPageId);

    /**
     * 获得页面列表
     *
     * @return {@link List}<{@link SysWebPageVO}>
     */
    List<SysWebPageVO> getPageList();
}

