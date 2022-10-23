package work.moonzs.base.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.enums.CacheConstants;
import work.moonzs.base.exception.ServiceException;
import work.moonzs.base.utils.JwtUtil;
import work.moonzs.base.utils.RedisUtil;
import work.moonzs.base.utils.WebUtil;
import work.moonzs.domain.ResponseResult;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 重复登录过滤器
 *
 * @author Moondust月尘
 */
@Component
public class RepeatLoginFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取解析token
        String jwt = parsingToken(request);
        // 如果token为空，或者是不用携带token的请求，则直接放行
        if (StrUtil.isEmpty(jwt) || "null".equalsIgnoreCase(jwt) || !JwtUtil.checkToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 如果token不为空，则进行验证，判断当前token有效期是否是redis中的之后
        if (isAccessAllow(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 不是的话意味着重复登录了
        logout(response);
    }

    /**
     * 注销，返回给前端响应
     *
     * @param response 响应
     */
    private void logout(HttpServletResponse response) {
        WebUtil.renderString(response, JSONUtil.toJsonStr(ResponseResult.fail(AppHttpCodeEnum.REPEAT_LOGIN)));
    }

    /**
     * 是否重复登录
     *
     * @param token 令牌
     * @return boolean
     */
    private boolean isAccessAllow(String token) {
        // 从token中解析用户id
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // token超时  token非法
            // 响应告诉前端需要重新登录
            throw new ServiceException(AppHttpCodeEnum.TOKEN_ABNORMAL);
        }
        String userId = claims.getSubject();
        String redisToken = (String) redisUtil.get(CacheConstants.TOKEN_KEY + userId);
        // 如果redis没有token，则为第一次登录，放行并加入redis
        if (StrUtil.isBlank(redisToken)) {
            redisUtil.set(CacheConstants.TOKEN_KEY + userId, token);
            return true;
        }
        // 如果和redis的token一样，当前用户登录，放行
        if (token.equals(redisToken)) {
            return true;
        }
        // redis中的token和用户token不一致，比较两个token的创建时间
        // 如果用户token大于redistoken，说明是新登录的，放行并更新redis
        // 否则用户token就是过期的，不放行
        Date date = JwtUtil.getIssueAt(token);
        Date redisDate = JwtUtil.getIssueAt(redisToken);
        if (date.after(redisDate)) {
            redisUtil.set(CacheConstants.TOKEN_KEY + userId, token);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析请求，获取token令牌
     *
     * @param request 请求
     */
    private String parsingToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return null;
    }
}
