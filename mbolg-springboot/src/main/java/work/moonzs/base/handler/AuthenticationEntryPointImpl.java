package work.moonzs.base.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.WebUtil;
import work.moonzs.domain.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author Moondust月尘
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // authException.printStackTrace();
        ResponseResult result = null;
        if (authException instanceof InsufficientAuthenticationException) {
            // 用户未登录
            result = ResponseResult.fail(AppHttpCodeEnum.USER_NEED_LOGIN);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            // 用户不存在
            result = ResponseResult.fail(AppHttpCodeEnum.USER_NOT_EXIST);
        } else if (authException instanceof BadCredentialsException) {
            // 用户名密码错误
            result = ResponseResult.fail(AppHttpCodeEnum.USER_FAILED_CERTIFICATION);
        } else {
            // TODO 不知名异常，等出现的时候再抛出
            result = ResponseResult.fail(45000, "认证又出异常啦，快来找问题");
        }
        // 响应给前端
        WebUtil.renderString(response, JSONUtil.toJsonStr(result));
    }
}
