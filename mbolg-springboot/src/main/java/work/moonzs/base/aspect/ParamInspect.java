package work.moonzs.base.aspect;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import work.moonzs.base.annotation.ParamCheck;
import work.moonzs.base.exception.ValidateException;
import work.moonzs.base.utils.VerifyUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置自定义校验的前置拦截器
 *
 * @author Moondust月尘
 */
@Aspect
@Component
public class ParamInspect {
    static {
        // TODO 应该符合springboot控制台输出的日志
        LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
    }

    private static final Log LOG = LogFactory.get();

    @Before(value = "@annotation(work.moonzs.base.annotation.ParamCheck)")
    public void before(JoinPoint point) {
        LOG.info(">>>>>Inspect.before");
        // 保存错误信息的
        StringBuilder errorMsg = new StringBuilder();
        // 访问目标方法的参数名称parameterNames和方法method
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        // 获取目标方法注解的类
        if (method.isAnnotationPresent(ParamCheck.class)) {
            // 目标注解class
            Class<?>[] clazzs = method.getAnnotation(ParamCheck.class).clazzs();
            // 目标注解参数名称
            String[] params = method.getAnnotation(ParamCheck.class).params();
            // 目标注解的组名
            Class<?>[] group = method.getAnnotation(ParamCheck.class).group();
            if (ArrayUtil.isEmpty(clazzs) || ArrayUtil.isEmpty(params)) {
                // 如果注解里面的参数为空，那么就把方法里面的参数进行校验，这里一般用的都是DTO，只会传一个参数，所以直接取[0]
                LOG.info("校验参数: {}", point.getArgs()[0]);
                VerifyUtil.validateField(errorMsg, point.getArgs()[0], group);
            } else {
                // 注解参数名
                String[] parameterNames = methodSignature.getParameterNames();
                List<String> parameters = ListUtil.toList(params);
                // 绑定注解类
                Map<String, Class<?>> clazzMap = this.bindClazz(parameters, clazzs);
                // 绑定参数
                Map<String, Object> parameterMap = this.bindParameter(parameterNames, point.getArgs());
                // ParamCheck注解中的两个参数数量不能为空并且保持一致
                if (clazzs.length > 0 && clazzs.length == parameters.size()) {
                    for (String parameter : parameterNames) {
                        if (parameters.contains(parameter)) {
                            LOG.info("校验对象: {}", clazzMap.get(parameter));
                            LOG.info("校验参数: {}", parameterMap.get(parameter));
                            VerifyUtil.validateField(errorMsg, parameterMap.get(parameter), group);
                        }
                    }
                }
            }
        }
        if (errorMsg.isEmpty()) {
            LOG.info("参数校验成功");
            LOG.info("<<<<<Inspect.before");
        } else {
            LOG.info("参数校验失败");
            LOG.info("<<<<<Inspect.before");
            throw new ValidateException(45000, errorMsg.toString());
        }
    }

    /**
     * 绑定注解类
     *
     * @param parameters 参数
     * @param clazzs     clazzs
     * @return {@link Map}<{@link String}, {@link Class}<{@link ?}>>
     */
    private Map<String, Class<?>> bindClazz(List<String> parameters, Class<?>[] clazzs) {
        HashMap<String, Class<?>> map = MapUtil.newHashMap();
        for (int i = 0; i < parameters.size(); i++) {
            map.put(parameters.get(i), clazzs[i]);
        }
        return map;
    }

    /**
     * 绑定参数
     *
     * @param parameterNames 参数名称
     * @param args           args
     */
    private Map<String, Object> bindParameter(String[] parameterNames, Object[] args) {
        Map<String, Object> map = MapUtil.newHashMap();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], args[i]);
        }
        return map;
    }
}
