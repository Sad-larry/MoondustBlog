package work.moonzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.base.utils.BeanCopyUtil;
import work.moonzs.domain.entity.AdminLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysAdminLogVO;
import work.moonzs.mapper.AdminLogMapper;
import work.moonzs.service.AdminLogService;

import java.util.List;

/**
 * 操作日志表(AdminLog)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:38:42
 */
@Service("adminLogService")
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    @Override
    public PageVO<SysAdminLogVO> listAdminLog(Integer pageNum, Integer pageSize) {
        Page<AdminLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AdminLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AdminLog::getCreateTime);
        page(page, queryWrapper);
        List<SysAdminLogVO> sysAdminLogVOS = BeanCopyUtil.copyBeanList(page.getRecords(), SysAdminLogVO.class);
        return new PageVO<>(sysAdminLogVOS, page.getTotal());
    }

    @Override
    public boolean deleteAdminLog(Long[] adminLogIds) {
        return removeBatchByIds(List.of(adminLogIds));
    }
}

