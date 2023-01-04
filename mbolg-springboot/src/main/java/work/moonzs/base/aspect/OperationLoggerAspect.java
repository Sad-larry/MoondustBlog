package work.moonzs.base.aspect;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import work.moonzs.base.annotation.OperationLogger;
import work.moonzs.base.utils.AspectUtil;
import work.moonzs.base.utils.IpUtil;
import work.moonzs.base.utils.SecurityUtil;
import work.moonzs.domain.entity.AdminLog;
import work.moonzs.domain.entity.ExceptionLog;
import work.moonzs.mapper.AdminLogMapper;
import work.moonzs.mapper.ExceptionLogMapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Moondust月尘
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLoggerAspect {
    private final ExceptionLogMapper exceptionLogMapper;
    private final AdminLogMapper adminLogMapper;
    private StopWatch operateTime;

    @Pointcut("@annotation(operationLogger)")
    public void pt(OperationLogger operationLogger) {

    }

    @Around(value = "pt(operationLogger)", argNames = "joinPoint,operationLogger")
    public Object operationMethod(ProceedingJoinPoint joinPoint, OperationLogger operationLogger) throws Throwable {
        operateTime = new StopWatch();
        operateTime.start();
        // 业务处理
        Object result = joinPoint.proceed();
        // 日志收集
        handle(joinPoint);
        return result;
    }

    /**
     * 异常日志收集
     *
     * @param joinPoint       切入点
     * @param operationLogger 操作日志记录器
     * @param e               e
     * @throws Exception 异常
     */
    @AfterThrowing(value = "pt(operationLogger)", throwing = "e", argNames = "joinPoint,operationLogger,e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLogger operationLogger, Throwable e) throws Exception {
        HttpServletRequest request = getHttpServletRequest();
        // 获取 IP 地址
        String ip = IpUtil.getIpAddr(request);
        // 获取操作名称字符串
        String operationName = AspectUtil.INSTANCE.parseParams(joinPoint.getArgs(), operationLogger.value());
        // 获取参数名称字符串
        String paramsJson = getParamsJson((ProceedingJoinPoint) joinPoint);
        // 获取操作用户
        String operationUsername = SecurityUtil.getUsername();
        // 封装异常日志
        ExceptionLog exceptionLog = ExceptionLog.builder()
                .username(operationUsername)
                .ip(ip)
                .ipSource(IpUtil.getCityInfo(ip))
                .method(joinPoint.getSignature().getName())
                .operation(operationName)
                .params(paramsJson)
                .exceptionJson(JSONUtil.toJsonStr(e.toString()))
                .exceptionMessage(e.getMessage())
                .build();
        // 将异常日志插入数据库中
        exceptionLogMapper.insert(exceptionLog);
    }

    /**
     * 管理员日志收集
     *
     * @param joinPoint 切入点
     */
    private void handle(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取被注解的方法
        Method method = AspectUtil.INSTANCE.getMethod(joinPoint);
        // 获取方法上的注解
        OperationLogger annotation = method.getAnnotation(OperationLogger.class);
        // 判断是否保存到数据库
        boolean saveDatabase = annotation.saveDatabase();
        if (!saveDatabase) {
            return;
        }
        // 获取操作名称字符串
        String operationName = AspectUtil.INSTANCE.parseParams(joinPoint.getArgs(), annotation.value());
        // 获取参数名称字符串
        String paramsJson = getParamsJson(joinPoint);
        // 获取当前操作用户
        String operationUsername = SecurityUtil.getUsername();
        HttpServletRequest request = getHttpServletRequest();
        // 获取方法类型
        String type = request.getMethod();
        // 获取 IP 地址
        String ip = IpUtil.getIpAddr(request);
        // 获取请求 URL
        String url = request.getRequestURI();

        // 程序运行时长
        operateTime.stop();
        long speedTime = operateTime.getLastTaskTimeMillis();
        // 封装操作日志
        AdminLog adminLog = AdminLog.builder()
                .username(operationUsername)
                .requestUrl(url)
                .type(type)
                .operationName(operationName)
                .ip(ip)
                .source(IpUtil.getCityInfo(ip))
                .spendTime(speedTime)
                .paramsJson(paramsJson)
                .classPath(joinPoint.getTarget().getClass().getName())
                .methodName(joinPoint.getSignature().getName())
                .build();
        // 将操作日志插入数据库中
        adminLogMapper.insert(adminLog);
    }

    /**
     * 获取参数json字符串
     *
     * @param joinPoint 连接点
     * @return {@link String}
     */
    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        HashMap<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        // 剔除参数名为 request 的参数
        boolean isContains = paramMap.containsKey("request");
        if (isContains) {
            paramMap.remove("request");
        }
        return JSONUtil.toJsonStr(paramMap);
    }

    /**
     * 获取 http servlet 请求
     *
     * @return {@link HttpServletRequest}
     */
    private HttpServletRequest getHttpServletRequest() {
        // 获取 RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取 RequestAttributes 中获取 HttpServletRequest 的信息
        return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }
}
