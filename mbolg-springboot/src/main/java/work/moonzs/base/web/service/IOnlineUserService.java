package work.moonzs.base.web.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.utils.RedisCache;
import work.moonzs.domain.entity.LoginUser;
import work.moonzs.domain.vo.PageVO;
import work.moonzs.domain.vo.sys.SysOnlineUserVO;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 在线用户统计服务
 *
 * @author Moondust月尘
 */
@Component
public class IOnlineUserService {
    @Autowired
    private RedisCache redisCache;

    /**
     * 设置在线用户
     *
     * @param userUid 用户uid
     */
    public void userOnline(String userUid) {
        redisCache.zsetSet(CacheConstants.ONLINE_USER_KEY, userUid, System.currentTimeMillis());
    }

    /**
     * 统计用户在线数
     *
     * @return {@link Long}
     */
    public Long userOnlineCount() {
        return redisCache.zsetCount(CacheConstants.ONLINE_USER_KEY);
    }

    /**
     * 用户下线
     *
     * @param userUid 用户uid
     */
    public Long userOffline(String userUid) {
        return redisCache.zsetRemove(CacheConstants.ONLINE_USER_KEY, userUid);
    }

    /**
     * 清除已经离线的用户
     * 默认清除单位为`分钟`
     *
     * @return {@link Long}
     */
    public Long userOfflineClean(int minute) {
        DateTime dateTime = DateTime.now();
        // 清除 x 分钟前未操作的在线用户
        DateTime offset = dateTime.offset(DateField.MINUTE, -minute);
        return redisCache.zsetRemove(CacheConstants.ONLINE_USER_KEY, 0L, offset.getTime());
    }

    /**
     * 用户在线列表
     *
     * @return {@link PageVO}<{@link SysOnlineUserVO}>
     */
    public PageVO<SysOnlineUserVO> userOnlineList() {
        Set<Object> keys = redisCache.zsetGet(CacheConstants.ONLINE_USER_KEY, 0, -1);
        List<SysOnlineUserVO> result = keys.stream().map(key -> {
            SysOnlineUserVO onlineUser = null;
            if (redisCache.hasKey(CacheConstants.LOGIN_USER_KEY + key)) {
                LoginUser loginUser = (LoginUser) redisCache.get(CacheConstants.LOGIN_USER_KEY + key);
                onlineUser = SysOnlineUserVO.builder()
                        .userId(loginUser.getUserId())
                        .userUid(loginUser.getUserUid())
                        .nickname(loginUser.getUser().getUsername())
                        .ipAddress(loginUser.getUser().getIpAddress())
                        .ipSource(loginUser.getUser().getIpSource())
                        .os(loginUser.getUser().getOs())
                        .browser(loginUser.getUser().getBrowser())
                        .loginTime(new Date(loginUser.getLoginTime()))
                        .lastActivityTime(loginUser.getUser().getLastLoginTime())
                        .build();
            }
            return onlineUser;
        }).collect(Collectors.toList());
        return new PageVO<>(result, Integer.toUnsignedLong(keys.size()));
    }
}
