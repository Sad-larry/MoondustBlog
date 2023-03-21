package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.entity.User;
import work.moonzs.domain.entity.UserAuth;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysOnlineUserVO;
import work.moonzs.domain.vo.sys.SysUserBaseVO;
import work.moonzs.domain.vo.sys.SysUserVO;
import work.moonzs.domain.vo.web.UserInfoVO;

import java.util.List;
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
     * 用户注销
     */
    void userLogout();

    /**
     * 用户列表
     *
     * @param pageNum   页面num
     * @param pageSize  页面大小
     * @param username  用户名
     * @param loginType 登录类型
     * @return {@link PageVO}<{@link SysUserVO}>
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

    List<UserInfoVO> getWebUserByIds(List<Long> userIds);


    /**
     * 更新用户
     *
     * @param user 用户
     * @return boolean
     */
    @Transactional
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userIds 用户id
     * @return boolean
     */
    @Transactional
    boolean deleteUser(Long[] userIds);

    /**
     * 发送电子邮件更新用户密码
     *
     * @param email 电子邮件
     */
    @Transactional
    void updatePasswordBySendEmail(String email);

    /**
     * 更新用户密码
     *
     * @param username    用户名
     * @param newPassword 新密码
     * @return boolean
     */
    @Transactional
    boolean updateUserPassword(String username, String newPassword);

    /**
     * 检查旧密码
     * 确定新旧密码是否一致
     *
     * @param oldPassword 旧密码
     * @param username    用户名
     */
    void checkOldPassword(String username, String oldPassword);

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
     * 注册方式有几种：1-账号密码,2-码云,3-Github,4-QQ,5-微信,6-微信小程序
     * 除了第一种方式需要使用邮箱登录，需要邮箱验证码验证
     *
     * @param user 用户
     * @return boolean
     */
    @Transactional
    boolean registerUser1(User user);

    @Transactional
    boolean registerUser6(String openId);

    /**
     * 发送注册邮件验证码
     *
     * @param username 用户名
     * @param mailUuid 邮件uuid
     * @param mailCode 邮件验证码
     */
    Map<String, Object> sendRegisterMailCode(String username, String mailUuid, String mailCode);

    /**
     * 简单的发送注册邮件验证码
     *
     * @param username 邮箱地址
     */
    void sendRegisterMailCode(String username);

    /**
     * 验证邮箱验证码
     *
     * @param username 用户名
     * @param mailCode 邮箱验证码
     */
    void validateMailCode(String username, String mailCode);

    /**
     * 验证密码邮件代码
     *
     * @param username 用户名
     * @param mailCode 邮件代码
     */
    void validatePasswordMailCode(String username, String mailCode);

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

    /**
     * 用户使用微信小程序登录
     *
     * @param code 临时登录凭证 code
     * @return {@link String} token
     */
    String wxmpLogin(String code);

    /**
     * 微信小程序用户信息
     *
     * @return {@link UserInfoVO}
     */
    UserInfoVO wxmpUserInfo();

    /**
     * 修改用户信息
     *
     * @return boolean
     */
    @Transactional
    boolean wxmpModify(UserAuth userAuth);
}

