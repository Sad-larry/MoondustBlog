package work.moonzs.base.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.utils.WebUtil;
import work.moonzs.domain.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // accessDeniedException.printStackTrace();
        // 权限不足异常  响应给前端
        WebUtil.renderString(response, JSONUtil.toJsonStr(ResponseResult.fail(AppHttpCodeEnum.NO_OPERATOR_AUTH)));
    }
}
