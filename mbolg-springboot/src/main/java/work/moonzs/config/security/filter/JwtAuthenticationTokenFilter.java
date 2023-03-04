package work.moonzs.config.security.filter;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import work.moonzs.base.web.service.ITokenService;
import work.moonzs.domain.entity.LoginUser;

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
    @Autowired
    private ITokenService iTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LoginUser loginUser = iTokenService.getLoginUser(request);
        // 如果用户没有携带token，也就没用登录用户，可能是不需要登录的接口，直接放行
        // 否则将登录用户的信息存入SecurityContext
        if (ObjectUtil.isNotNull(loginUser)) {
            // 验证用户令牌是否在有效期
            boolean isVerify = iTokenService.verifyToken(loginUser);
            // 有效则将用户信息从redis中读取出来并存入SecurityContext
            if (isVerify) {
                // 存入SecurityContextHolder
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
                // 设置服务认证细节，Authentication.getDetails()获取的就是WebAuthenticationDetails，获取ip，sessionId
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
