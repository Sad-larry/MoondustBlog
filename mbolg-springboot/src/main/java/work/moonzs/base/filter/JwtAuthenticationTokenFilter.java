package work.moonzs.base.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.JwtUtil;
import work.moonzs.base.utils.WebUtils;
import work.moonzs.domain.ResponseResult;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录认证过滤器
 *
 * @author Moondust月尘
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            // 如果用户没有携带token，可能是不需要登录的接口，其他可能会报异常
            filterChain.doFilter(request, response);
            // 用户认证之后，返回来不需要执行后面代码
            return;
        }
        // 从token中解析用户id
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // token超时  token非法
            // 响应告诉前端需要重新登录
            WebUtils.renderString(response, JSONUtil.toJsonStr(ResponseResult.fail(AppHttpCodeEnum.TOKEN_ABNORMAL)));
            return;
        }
        String userId = claims.getSubject();
        // TODO 从redis中读取用户数据

        // 如果取不到说明登录过期

        // TODO 暂时存userId 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
