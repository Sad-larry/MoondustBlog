package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.FeedBack;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.sys.SysFeedBackVo;

/**
 * 用户反馈表(FeedBack)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:41
 */
public interface FeedBackService extends IService<FeedBack> {

    /**
     * 反馈列表
     *
     * @param type 类型
     * @return {@link PageVo}<{@link SysFeedBackVo}>
     */
    PageVo<SysFeedBackVo> listFeedBack(Integer type);

    /**
     * 删除反馈
     *
     * @param feedBackIds 反馈id
     * @return boolean
     */
    @Transactional
    boolean deleteFeedBack(Long[] feedBackIds);
}

