package work.moonzs.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Moondust月尘
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 解决跨域问题
     *
     * @param registry 注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
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

    /**
     * 配置消息转换器
     *
     * @param converters 转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 删除 Jackson 转换器，配置 fastJson 转换器
        for (int i = converters.size() - 1; i >= 0; i--) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(i);
            }
        }
        FastJsonHttpMessageConverter fastjsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 数据序列化管理
        fastJsonConfig.setSerializerFeatures(
                // 输出 null 字段信息
                SerializerFeature.WriteMapNullValue,
                // 将空集转换为 [] 输出
                SerializerFeature.WriteNullListAsEmpty,
                // 字符串 null 替换为空串
                SerializerFeature.WriteNullStringAsEmpty,
                // 数值类型的 null 替换为0
                SerializerFeature.WriteNullNumberAsZero,
                // 日期数据格式化
                SerializerFeature.WriteDateUseDateFormat,
                // 禁用循环引用
                SerializerFeature.DisableCircularReferenceDetect
        );
        // 设置 UTF-8 字符编码
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastjsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        // 数据返回格式为 application/json
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastjsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        // 添加转换器
        converters.add(fastjsonHttpMessageConverter);
    }
}
