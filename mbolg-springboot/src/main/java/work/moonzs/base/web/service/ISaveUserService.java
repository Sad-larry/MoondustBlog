package work.moonzs.base.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.entity.LoginUser;

import java.util.List;

/**
 * 利用缓存记录发放token的用户uid
 *
 * @author Moondust月尘
 */
@Component
@Slf4j
public class ISaveUserService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ITokenService iTokenService;

    /**
     * 保存用户令牌
     *
     * @param userUid 用户uid
     */
    public void saveUserToken(String userUid) {
        redisCache.sSet(CacheConstants.SAVE_LOGIN_USER_KEY, userUid);
    }

    /**
     * 删除用户令牌
     *
     * @param userUid 用户uid
     */
    public void removeUserToken(String userUid) {
        if (redisCache.sHasKey(CacheConstants.SAVE_LOGIN_USER_KEY, userUid)) {
            // 删除 loginUser 数据再删除 userUid 集合中的数据
            iTokenService.delLoginUser(userUid);
            redisCache.setRemove(CacheConstants.SAVE_LOGIN_USER_KEY, userUid);
        }
    }

    /**
     * 随机删除用户令牌
     */
    public void randomRemoveUserToken() {
        long removeCount = 10;
        long setSize = redisCache.sGetSetSize(CacheConstants.SAVE_LOGIN_USER_KEY);
        // redis缓存最多保存多少位
        if (setSize > 64) {
            removeCount = setSize / 4;
        }
        List<Object> keys = redisCache.sGetRandomMembers(CacheConstants.SAVE_LOGIN_USER_KEY, removeCount);
        keys.forEach(key -> {
            if (redisCache.hasKey(CacheConstants.LOGIN_USER_KEY + key)) {
                LoginUser loginUser = (LoginUser) redisCache.get(CacheConstants.LOGIN_USER_KEY + key);
                // 若用户的过期时间小于当前时间，则进行删除操作
                if (loginUser.getExpireTime() < System.currentTimeMillis()) {
                    removeUserToken(key.toString());
                    // log.info("remove expire token key : {}", key);
                }
            }
        });
    }
}
