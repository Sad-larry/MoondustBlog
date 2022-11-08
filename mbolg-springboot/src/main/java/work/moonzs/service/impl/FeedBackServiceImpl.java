package work.moonzs.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.enums.SystemConstants;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.FeedBack;
import work.moonzs.domain.vo.PageVo;
import work.moonzs.domain.vo.sys.SysFeedBackVo;
import work.moonzs.mapper.FeedBackMapper;
import work.moonzs.service.FeedBackService;

import java.util.List;

/**
 * 用户反馈表(FeedBack)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("feedBackService")
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    @Override
    public PageVo<SysFeedBackVo> listFeedBack(Integer type) {
        LambdaQueryWrapper<FeedBack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(type), FeedBack::getType, type);
        queryWrapper.orderByDesc(FeedBack::getCreateTime);
        Page<FeedBack> page = new Page<>(SystemConstants.PAGE_NUM, SystemConstants.PAGE_SIZE);
        page(page, queryWrapper);
        List<SysFeedBackVo> sysFeedBackVos = BeanCopyUtil.copyBeanList(page.getRecords(), SysFeedBackVo.class);
        return new PageVo<>(sysFeedBackVos, page);
    }

    @Override
    public boolean deleteFeedBack(Long[] feedBackIds) {
        return removeBatchByIds(List.of(feedBackIds));
    }
}

