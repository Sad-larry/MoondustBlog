package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import work.moonzs.domain.entity.UserLog;

import java.util.Map;

/**
 * 日志表(UserLog)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 13:58:37
 */
public interface UserLogMapper extends BaseMapper<UserLog> {

    /**
     * 列表每周
     *
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @MapKey("createTime")
    Map<String, Object> listWeeklyVisits(String date);
}

