package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;

/**
 * 用户基础信息表(User)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:37:18
 */
public interface UserService extends IService<User> {

    /**
     * 管理员登录，拿取指定令牌
     *
     * @param username 用户名
     * @param password 密码
     * @param uuid     uuid
     * @param code     代码
     * @return {@link String}
     */
    @Transactional
    String adminLogin(String username, String password, String uuid, String code);

    /**
     * 管理员注销
     */
    void adminLogout();

    /**
     * 用户列表
     *
     * @param pageNum   页面num
     * @param pageSize  页面大小
     * @param username  用户名
     * @param loginType 登录类型
     * @return {@link PageVO}<{@link UserListVo}>
     */
    PageVO<SysUserVO> listUser(Integer pageNum, Integer pageSize, String username, Integer loginType);

    /**
     * 保存用户
     *
     * @param user 用户
     * @return {@link Long}
     */
    Long saveUser(User user);

    /**
     * 通过用户id查询用户的基本信息
     *
     * @param userId 用户id
     * @return {@link Object}
     */
    SysUserBaseVO getUserById(Long userId);

    /**
     * 删除用户
     *
     * @param userIds 用户id
     * @return boolean
     */
    @Transactional
    boolean deleteUser(Long[] userIds);
}

