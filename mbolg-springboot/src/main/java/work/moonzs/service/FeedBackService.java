package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.FeedBack;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysFeedBackVO;

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
     * @return {@link PageVO}<{@link SysFeedBackVO}>
     */
    PageVO<SysFeedBackVO> listFeedBack(Integer type);

    /**
     * 删除反馈
     *
     * @param feedBackIds 反馈id
     * @return boolean
     */
    @Transactional
    boolean deleteFeedBack(Long[] feedBackIds);

    /**
     * 添加反馈
     *
     * @param feedBack 反馈
     */
    @Transactional
    Long addWebFeedback(FeedBack feedBack);

    /**
     * 回复反馈
     *
     * @param feedBack     反馈
     * @param replyContent 回复内容
     */
    void replyFeedBack(FeedBack feedBack, String replyContent);
}

