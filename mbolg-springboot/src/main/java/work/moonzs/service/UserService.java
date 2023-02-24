package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.UserListVo;
import work.moonzs.domain.vo.sys.SysOnlineUserVO;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;

import java.util.Map;

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

    /**
     * 在线用户列表
     *
     * @return {@link PageVO}<{@link SysOnlineUserVO}>
     */
    PageVO<SysOnlineUserVO> listOnlineUsers();

    /**
     * 强制用户下线
     * 删除在线列表和登录记录中的数据
     *
     * @param userUid 用户uid
     */
    void kick(String userUid);

    /**
     * 注册用户
     * 注册方式有几种：1-账号密码,2-码云,3-Github,4-QQ,5-微信
     * 除了第一种方式需要使用邮箱登录，需要邮箱验证码验证
     *
     * @param user 用户
     * @return boolean
     */
    @Transactional
    boolean registerUser(User user);

    /**
     * 发送注册邮件验证码
     *
     * @param username 用户名
     * @param mailUuid 邮件uuid
     * @param mailCode 邮件验证码
     */
    Map<String, Object> sendRegisterMailCode(String username, String mailUuid, String mailCode);

    /**
     * 判断用户是否已经注册
     *
     * @param username 用户名
     * @return boolean
     */
    boolean alreadyRegister(String username);

    /**
     * 用户登录
     *
     * @param user 用户
     * @return {@link String}
     */
    User userLogin(User user);
}

