package work.moonzs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义元数据配置
 *
 * @author Moondust月尘
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxmp")
public class CustomPropertiesWxmp {
    /**
     * 请求的网址
     */
    private String loginUrl;
    /**
     * 小程序 appId
     */
    private String loginAppid;
    /**
     * 小程序 appSecret
     */
    private String loginSecret;
    /**
     * 授权类型,固定参数
     */
    private String loginGrantType;
}
