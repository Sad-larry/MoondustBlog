package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.ExceptionLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysExceptionLogVO;
import work.moonzs.mapper.ExceptionLogMapper;
import work.moonzs.service.ExceptionLogService;

import java.util.List;

/**
 * 异常日志表(ExceptionLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:16
 */
@Service("exceptionLogService")
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public PageVO<SysExceptionLogVO> listExceptionLog(Integer pageNum, Integer pageSize) {
        Page<ExceptionLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExceptionLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ExceptionLog::getCreateTime);
        page(page, queryWrapper);
        List<SysExceptionLogVO> sysExceptionLogVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysExceptionLogVO.class);
        return new PageVO<>(sysExceptionLogVOS, page.getTotal());
    }

    @Override
    public boolean deleteExceptionLog(Long[] exceptionLogIds) {
        return removeBatchByIds(List.of(exceptionLogIds));
    }
}

