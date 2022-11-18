package work.moonzs.base.quartz;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import work.moonzs.domain.entity.Job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具
 * 原理就是将一个字符串拆解出类名，方法名，方法参数类型，方法参数值
 * com.example.A.foo('string', 1L, 1.1D, true)或者a.foo都可
 * 类名可以解析类的全包名或者spring bean中的名字，最终结果就是调用该类中的方法
 * 方法名就是任务执行的具体方法
 * 方法参数类型以及方法参数值可不填写，填写时一定要按规则填，没有浮点类型，只有双精度浮点类型
 *
 * @author Moondust月尘
 */
public class JobInvokeUtil {
    /**
     * 执行方法
     *
     * @param job 任务
     */
    public static void invokeMethod(Job job) throws Exception {
        // 获取任务的调用目标字符串，通常为类中的某个方法，Class.method形式
        String invokeTarget = job.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);

        if (!isValidClassName(beanName)) {
            Object bean = SpringUtil.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName).getConstructor().newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     * @throws NoSuchMethodException     没有这样方法异常
     * @throws InvocationTargetException 调用目标异常
     * @throws IllegalAccessException    非法访问异常
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (methodParams != null && methodParams.size() > 0) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }

    /**
     * 校验是否为为class包名
     *
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
        return StrUtil.count(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget) {
        // 取出'('之前的字符串，一般为除了方法参数的名字，若字符串中没有'('则返回原先字符串
        String beanName = StrUtil.subBefore(invokeTarget, "(", false);
        // 取出'.'之前的字符串，一般为类bean的名字
        return StrUtil.subBefore(beanName, ".", true);
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StrUtil.subBefore(invokeTarget, "(", false);
        // 取出'.'之后的字符串，一般为方法的名字
        return StrUtil.subAfter(methodName, ".", true);
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StrUtil.subBetween(invokeTarget, "(", ")");
        if (StrUtil.isEmpty(methodStr)) {
            return null;
        }
        // 以','切割方法中的参数
        String[] methodParams = methodStr.split(",");
        List<Object[]> classs = new LinkedList<>();
        for (String methodParam : methodParams) {
            // 除去字符串头尾部的空白，如果字符串是null，返回""
            String str = StrUtil.trimToEmpty(methodParam);
            // String字符串类型，包含'
            if (StrUtil.contains(str, "'")) {
                classs.add(new Object[]{StrUtil.replace(str, "'", ""), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if (StrUtil.equals(str, "true") || StrUtil.equalsIgnoreCase(str, "false")) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，包含L
            else if (StrUtil.containsIgnoreCase(str, "L")) {
                classs.add(new Object[]{Long.valueOf(StrUtil.replaceIgnoreCase(str, "L", "")), Long.class});
            }
            // double浮点类型，包含D
            else if (StrUtil.containsIgnoreCase(str, "D")) {
                classs.add(new Object[]{Double.valueOf(StrUtil.replaceIgnoreCase(str, "D", "")), Double.class});
            }
            // 其他类型归类为整形
            else {
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            index++;
        }
        return classs;
    }
}
