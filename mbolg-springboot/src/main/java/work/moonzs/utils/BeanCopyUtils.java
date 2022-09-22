package work.moonzs.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具类，负责Bean对象的复制
 *
 * @author Moondust月尘
 */
public class BeanCopyUtils {
    /**
     * 拷贝单个对象的bean
     *
     * @param source 源
     * @param clazz  clazz
     * @return {@link T}
     */
    public static <T> T copyBean(Object source, Class<T> clazz) {
        // 创建目标对象
        T result = null;
        try {
            // 反射创建对象实例
            result = clazz.getDeclaredConstructor().newInstance();
            // 实现属性copy
            BeanUtils.copyProperties(source, result);
            // 返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 拷贝bena集合
     *
     * @param list  列表
     * @param clazz clazz
     * @return {@link List}<{@link V}>
     */
    public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
        return list.stream()
                // 调用copyBean方法将list流中的每个对象实行操作
                .map(o -> copyBean(o, clazz))
                // 将数据收集起来并转化为list
                .collect(Collectors.toList());
    }
}
