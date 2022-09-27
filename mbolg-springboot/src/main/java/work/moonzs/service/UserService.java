package work.moonzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
    ResponseResult<?> adminLogin(User user);
}

