package work.moonzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.moonzs.domain.entity.UserAuth;
import work.moonzs.mapper.UserAuthMapper;
import work.moonzs.service.UserAuthService;

/**
 * 用户信息表(UserAuth)表服务实现类
 *
 * @author Moondust月尘
 * @since 2022-10-30 10:39:33
 */
@Service("userAuthService")
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

}

