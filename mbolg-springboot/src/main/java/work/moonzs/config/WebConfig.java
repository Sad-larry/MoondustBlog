package work.moonzs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Moondust月尘
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    /**
     * 解决跨域问题
     *
     * @param registry 注册表
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // 设置允许请求路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 设置是否允许Cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
