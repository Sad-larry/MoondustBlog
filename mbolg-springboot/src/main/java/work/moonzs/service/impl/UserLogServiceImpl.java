package work.moonzs.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.UserLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysUserLogVO;
import work.moonzs.mapper.UserLogMapper;
import work.moonzs.service.UserLogService;

import java.util.List;
import java.util.Map;

/**
 * 日志表(UserLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 13:58:37
 */
@Service("userLogService")
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Override
    public PageVO<SysUserLogVO> listUserLog(Integer pageNum, Integer pageSize) {
        Page<UserLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserLog::getCreateTime);
        page(page, queryWrapper);
        List<SysUserLogVO> sysUserLogVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysUserLogVO.class);
        return new PageVO<>(sysUserLogVOS, page.getTotal());
    }

    @Override
    public boolean deleteUserLog(Long[] userLogIds) {
        return removeBatchByIds(List.of(userLogIds));
    }

    @Override
    public Map<String, Object> getWeeklyVisits() {
        DateTime dateTime = DateUtil.offsetWeek(DateUtil.date(), -1);
        return baseMapper.listWeeklyVisits(dateTime.toDateStr());
    }
}

