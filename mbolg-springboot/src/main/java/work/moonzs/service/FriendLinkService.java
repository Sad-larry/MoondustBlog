package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.FriendLink;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysFriendLinkVO;

/**
 * 友情链接表(FriendLink)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 友链列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param name     名字
     * @param status   状态
     * @return {@link PageVO}<{@link SysFriendLinkVO}>
     */
    PageVO<SysFriendLinkVO> listFriendLink(Integer pageNum, Integer pageSize, String name, Integer status);


    /**
     * 添加友链
     *
     * @param friendLink 友链
     * @return boolean
     */
    @Transactional
    Long insertFriendLink(FriendLink friendLink);

    /**
     * 更新友链
     *
     * @param friendLink 友链
     * @return boolean
     */
    @Transactional
    boolean updateFriendLink(FriendLink friendLink);

    /**
     * 删除友链
     *
     * @param friendLinkIds 友链ids
     * @return boolean
     */
    @Transactional
    boolean deleteFriendLink(Long[] friendLinkIds);

    /**
     * 置顶友链
     *
     * @param friendLinkId 友链id
     */
    @Transactional
    void topFriendLink(Long friendLinkId);

    /**
     * 通过审核
     *
     * @param friendLinkIds 友链ids
     */
    @Transactional
    void passFriendLink(Long[] friendLinkIds);
}

