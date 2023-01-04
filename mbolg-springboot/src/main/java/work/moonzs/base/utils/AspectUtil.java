package work.moonzs.base.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AOP 相关工具类
 *
 * @author Moondust月尘
 */
public enum AspectUtil {
    /**
     * 实例
     */
    INSTANCE;

    /**
     * 获取以类路径为前缀的键
     *
     * @param joinPoint 连接点
     * @param prefix    前缀
     * @return {@link String}
     */
    public String getKey(JoinPoint joinPoint, String prefix) {
        StringBuilder keyPrefix = new StringBuilder();
        if (!StrUtil.isEmpty(prefix)) {
            keyPrefix.append(prefix);
        }
        keyPrefix.append(getClassName(joinPoint));
        return keyPrefix.toString();
    }

    /**
     * 获取当前切面执行的方法所在的类
     *
     * @param joinPoint 连接点
     * @return {@link String}
     */
    public String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName().replaceAll("\\.", "_");
    }

    /**
     * 获取当前切面执行的方法的方法名
     *
     * @param joinPoint 连接点
     * @return {@link Method}
     */
    public Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        return target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


    /**
     * 解析获取当前切面执行的方法参数
     *
     * @param params        参数个数
     * @param bussinessName 业务名称
     * @return {@link String}
     */
    public String parseParams(Object[] params, String bussinessName) {
        if (bussinessName.contains("{") && bussinessName.contains("}")) {
            List<String> strings = match(bussinessName, "(?<=\\{)(\\d+)");
            for (String string : strings) {
                int index = Integer.parseInt(string);
                bussinessName = bussinessName.replaceAll("{" + index + "}", JSONUtil.toJsonStr(params[index - 1]));
            }
        }
        return bussinessName;
    }

    /**
     * @param str   要匹配的字符串
     * @param regex 正则表达式字符串
     * @return {@link List}<{@link String}>
     */
    public static List<String> match(String str, String regex) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
