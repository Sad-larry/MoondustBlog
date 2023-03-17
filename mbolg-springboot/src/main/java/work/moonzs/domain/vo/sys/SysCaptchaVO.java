package work.moonzs.domain.vo.sys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Moondust月尘
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysCaptchaVO {
    /**
     * 是否开启验证码功能
     */
    private Boolean captchaEnabled;
    /**
     * 给用户随机生成的标识
     */
    private String uuid;
    /**
     * Base64图片
     */
    private String img;
}
