package work.moonzs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;

import java.util.List;

/**
 * 用户基础信息表(User)表数据库访问层
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:34:52
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户列表页面
     *
     * @param page      页面
     * @param username  用户名
     * @param loginType 登录类型
     * @return {@link List}<{@link SysUserVO}>
     */
    List<SysUserVO> listUserPage(@Param("page") Page<User> page, @Param("username") String username, @Param("loginType") Integer loginType);

    /**
     * 得到用户id
     *
     * @param userId 用户id
     * @return {@link SysUserBaseVO}
     */
    SysUserBaseVO getUserById(Long userId);
}

