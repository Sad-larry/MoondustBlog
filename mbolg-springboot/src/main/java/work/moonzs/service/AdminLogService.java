package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.AdminLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysAdminLogVO;

/**
 * 操作日志表(AdminLog)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:36:01
 */
public interface AdminLogService extends IService<AdminLog> {

    /**
     * 操作日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVO}<{@link SysAdminLogVO}>
     */
    PageVO<SysAdminLogVO> listAdminLog(Integer pageNum, Integer pageSize);

    /**
     * 批量删除操作日志
     *
     * @param adminLogIds 操作日志id
     * @return boolean
     */
    @Transactional
    boolean deleteAdminLog(Long[] adminLogIds);
}

