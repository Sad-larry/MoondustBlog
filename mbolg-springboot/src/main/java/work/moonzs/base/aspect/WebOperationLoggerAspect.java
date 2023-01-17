package work.moonzs.base.aspect;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import work.moonzs.base.annotation.WebOperationLogger;
import work.moonzs.base.utils.AspectUtil;
import work.moonzs.base.utils.IpUtil;
import work.moonzs.base.utils.WebUtil;
import work.moonzs.domain.ResponseResult;
import work.moonzs.domain.entity.UserLog;
import work.moonzs.mapper.UserLogMapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Moondust月尘
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class WebOperationLoggerAspect {
    private final UserLogMapper userLogMapper;

    @Pointcut("@annotation(webOperationLogger)")
    public void pt(WebOperationLogger webOperationLogger) {

    }

    @Around(value = "pt(webOperationLogger)", argNames = "joinPoint,webOperationLogger")
    public Object operationMethod(ProceedingJoinPoint joinPoint, WebOperationLogger webOperationLogger) throws Throwable {
        // 业务处理
        Object result = joinPoint.proceed();
        // 日志收集
        handle(joinPoint, (ResponseResult) result);
        return result;
    }

    /**
     * 日志收集具体方法
     *
     * @param joinPoint 切入点
     * @param result    方法返回结果
     */
    private void handle(ProceedingJoinPoint joinPoint, ResponseResult result) throws NoSuchMethodException {
        // 获取被注解的方法
        Method method = AspectUtil.INSTANCE.getMethod(joinPoint);
        // 获取方法上的注解
        WebOperationLogger annotation = method.getAnnotation(WebOperationLogger.class);
        // 判断是否保存到数据库
        boolean saveDatabase = annotation.saveDatabase();
        if (!saveDatabase) {
            return;
        }
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        // 获取 IP 地址
        String ip = IpUtil.getIpAddr(request);
        // 获取请求的 User-Agent 字段
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("user-agent"));
        // 解析客户端类型
        String clientType = userAgent.getPlatform().toString();
        // 解析客户端操作系统类型
        String os = userAgent.getOs().toString();
        // 解析浏览器
        String browser = userAgent.getBrowser().toString();
        // 封装操作日志
        UserLog userLog = UserLog.builder()
                .ip(ip)
                .address(IpUtil.getCityInfo(ip))
                .type(annotation.type())
                .description(annotation.desc())
                .model(annotation.value())
                .result(result.get(ResponseResult.MSG_TAG).toString())
                .accessOs(os)
                .browser(browser)
                .clientType(clientType)
                .build();
        userLogMapper.insert(userLog);
    }
}
