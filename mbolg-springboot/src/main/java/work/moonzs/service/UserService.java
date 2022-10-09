package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.User;

/**
 * (User)表服务接口
 *
 * @author Moondust月尘
 * @since 2022-09-27 14:48:04
 */
public interface UserService extends IService<User> {

    /**
     * 管理员登录
     *
     * @param user 用户
     * @return {@link ResponseResult}<{@link ?}>
     */
    @Transactional
    ResponseResult<?> adminLogin(User user);

    /**
     * 管理员注销
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> adminLogout();

    /**
     * 用户列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param fuzzyField 模糊领域
     * @return {@link ResponseResult}<{@link ?}>
     */
    ResponseResult<?> listUsers(Integer pageNum, Integer pageSize, String fuzzyField);

    /**
     * 保存用户
     *
     * @param user 用户
     * @return {@link Long}
     */
    Long saveUser(User user);
}

