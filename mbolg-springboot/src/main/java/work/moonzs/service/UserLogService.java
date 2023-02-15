package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.UserLog;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysUserLogVO;

import java.util.Map;

/**
 * 日志表(UserLog)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 13:58:37
 */
public interface UserLogService extends IService<UserLog> {
    /**
     * 用户日志列表
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link PageVO}<{@link SysUserLogVO}>
     */
    PageVO<SysUserLogVO> listUserLog(Integer pageNum, Integer pageSize);

    /**
     * 批量删除用户日志
     *
     * @param userLogIds 用户日志id
     * @return boolean
     */
    @Transactional
    boolean deleteUserLog(Long[] userLogIds);

    /**
     * 列表每周访问量
     *
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getWeeklyVisits();
}

