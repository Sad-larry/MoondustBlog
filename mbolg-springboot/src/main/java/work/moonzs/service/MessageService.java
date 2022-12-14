package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.Message;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysMessageVO;
import work.moonzs.domain.vo.web.MessageVO;

import java.util.List;

/**
 * 留言板表(Message)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:45
 */
public interface MessageService extends IService<Message> {

    /**
     * 留言列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link PageVO}<{@link SysMessageVO}>
     */
    PageVO<SysMessageVO> listMessage(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 审核通过留言
     *
     * @param messageIds 留言id
     * @return boolean
     */
    @Transactional
    boolean passMessage(Long[] messageIds);

    /**
     * 删除留言
     *
     * @param messageIds 留言id
     * @return boolean
     */
    @Transactional
    boolean deleteMessage(Long[] messageIds);

    /**
     * web留言列表
     *
     * @return {@link MessageVO}
     */
    List<MessageVO> listWebMessage();

    /**
     * 添加留言
     *
     * @param message 留言
     * @return {@link Long}
     */
    @Transactional
    Long addWebMessage(Message message);
}

