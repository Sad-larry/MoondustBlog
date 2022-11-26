package work.moonzs.base.aspect;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.moonzs.base.annotation.SystemLog;

import javax.servlet.http.HttpServletRequest;

/**
 * 好像这个注解只对方法中的参数为常见数据类型才有用
 * 像 MultipartFile 这种会报错，我觉得应该是`打印请求入参`这块JSON对该数据序例化不了
 *
 * @author Moondust月尘
 */
@Aspect
@Slf4j
public class LogAspect {
    /**
     * 切点为 SystemLog 类
     */
    @Pointcut("@annotation(work.moonzs.base.annotation.SystemLog)")
    public void pt() {
    }

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======");
        }
        return ret;
    }

    /**
     * 前处理
     *
     * @param joinPoint 连接点
     */
    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取被增强方法上的注解
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}", request.getRequestURI());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSONUtil.toJsonStr(joinPoint.getArgs()));
    }

    /**
     * 处理后
     *
     * @param ret 对象
     */
    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSONUtil.toJsonStr(ret));
    }

    /**
     * 获取被增强方法上的注解
     *
     * @param joinPoint 连接点
     * @return {@link SystemLog}
     */
    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(SystemLog.class);
    }
}
