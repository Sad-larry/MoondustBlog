package work.moonzs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.StatusConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.Message;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysMessageVO;
import work.moonzs.mapper.MessageMapper;
import work.moonzs.service.MessageService;

import java.util.List;

/**
 * 留言板表(Message)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:20
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public PageVO<SysMessageVO> listMessage(Integer pageNum, Integer pageSize, String fuzzyField) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(fuzzyField), Message::getNickname, fuzzyField);
        queryWrapper.orderByAsc(Message::getStatus);
        queryWrapper.orderByAsc(Message::getTime);
        Page<Message> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<SysMessageVO> sysMessageVos = BeanCopyUtil.copyBeanList(page.getRecords(), SysMessageVO.class);
        return new PageVO<>(sysMessageVos, page);
    }

    @Override
    public boolean passMessage(Long[] messageIds) {
        LambdaUpdateWrapper<Message> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.in(Message::getId, List.of(messageIds));
        queryWrapper.set(Message::getStatus, StatusConstants.NORMAL);
        return update(queryWrapper);
    }

    @Override
    public boolean deleteMessage(Long[] messageIds) {
        return removeBatchByIds(List.of(messageIds));
    }
}

