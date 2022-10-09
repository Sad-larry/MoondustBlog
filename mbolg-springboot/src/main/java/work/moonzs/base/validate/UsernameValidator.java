package work.moonzs.base.validate;

import work.moonzs.base.annotation.UsernameValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 这里的 value 就是输入参数（证件号），例如：230102197701013467
        if (!"md".equals(value)) {
            return false;
        }
        // TODO 根据自己的实际业务情况，编写校验逻辑，返回校验结果
        return true;
    }
} 
