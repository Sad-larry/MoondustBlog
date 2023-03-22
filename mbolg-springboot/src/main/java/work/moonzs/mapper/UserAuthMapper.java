package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import work.moonzs.domain.entity.UserAuth;

/**
 * 用户信息表(UserAuth)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:25:53
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 按用户id批量删除
     *
     * @param userIds 用户id
     * @return boolean
     */
    boolean deleteByUserIds(@Param("userIds") Long[] userIds);

    /**
     * 按用户id查找
     *
     * @param userId 用户id
     * @return boolean
     */
    UserAuth getByUserId(Long userId);
}

