package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.User;
import work.moonzs.mapper.UserMapper;
import work.moonzs.service.UserService;

/**
 * (User)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-09-21 19:37:18
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

