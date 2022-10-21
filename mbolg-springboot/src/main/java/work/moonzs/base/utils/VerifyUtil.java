package work.moonzs.base.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


/**
 * 参数校验器
 *
 * @author zhangYiCong
 * @date 2021-07-13 15:31:16
 */
public class VerifyUtil {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证字段
     * 传入对象,校验被validation注解标注的参数
     *
     * @param request  需要校验的类
     * @param groups   分组校验
     * @param errorMsg 错误消息
     */
    public static <T> void validateField(StringBuilder errorMsg, T request, Class<?>... groups) {
        // 获取校验器
        Validator validator = factory.getValidator();
        // 校验传入的对象被validation注解标注的参数
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(request, groups);
        // 遍历违反约束的对象,拼接错误原因
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            errorMsg.append(constraintViolation.getMessage()).append(",");
        }
        // 去掉强迫症的逗号
        if (!errorMsg.isEmpty()) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
        }
    }
}
