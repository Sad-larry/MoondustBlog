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
@ConfigurationProperties(prefix = "oss.qiniu")
public class CustomProperties {
    /**
     * 七牛云公有密钥
     */
    private String accessKey;
    /**
     * 七牛云私有密钥
     */
    private String secretKey;
    /**
     * 域名地址
     */
    private String domain;
    /**
     * 七牛云空间名
     */
    private String bucket;
}
