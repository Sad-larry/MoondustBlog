package work.moonzs.base.qiniu.config;

import com.qiniu.cdn.CdnManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;

/**
 * 七牛云的上传配置
 *
 * @author Moondust月尘
 */
@Configuration
@ConditionalOnClass({Servlet.class, MultipartConfigElement.class, StandardServletMultipartResolver.class})
// 控制配置类是否生效
@ConditionalOnProperty(prefix = "spring.servlet.multipart", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MultipartProperties.class)
public class QiniuConfig {
    @Value("${oss.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${oss.qiniu.secret-key}")
    private String SECRET_KEY;

    private final MultipartProperties multipartProperties;

    @Autowired
    public QiniuConfig(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    /**
     * 上传配置
     *
     * @return {@link MultipartConfigElement}
     */
    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {
        return this.multipartProperties.createMultipartConfig();
    }

    /**
     * 注册解析器
     *
     * @return {@link StandardServletMultipartResolver}
     */
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }

    /**
     * qiniu配置，我的空间是 华东-浙江2 机房
     * 原先我用 huadongZheJiang2() 竟然不行，还是归属于华东
     *
     * @return {@link Configuration}
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfiguration() {
        return new com.qiniu.storage.Configuration(Region.autoRegion());
    }

    /**
     * 构建一个七牛云上传工具实例
     *
     * @return {@link UploadManager}
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfiguration());
    }

    /**
     * 认证信息实例
     *
     * @return {@link Auth}
     */
    @Bean
    public Auth auth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }

    /**
     * 构建七牛空间管理实例
     *
     * @return {@link BucketManager}
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qiniuConfiguration());
    }


    /**
     * 构建七牛cdn管理实例
     *
     * @return {@link CdnManager}
     */
    @Bean
    public CdnManager cdnManager() {
        return new CdnManager(auth());
    }
}
