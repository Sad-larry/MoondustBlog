package work.moonzs.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

/**
 * 说明：
 *
 * @author sheng
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    /**
     * 将 RedisTemplate 对象注册到容器中
     *
     * @param redisConnectionFactory RedisConnectionFactory 工厂
     * @return RedisTemplate 对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 对 key 进行序列化
        // StringRedisSerializer 当需要存储 String 类型的 key 时序列化使用
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 对 String 的 key 进行序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 对 Hash 的 key 进行序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        /*
            2. 对值进行序列化
            也可以使用 com.alibaba.fastjson.support.spring.FastJsonRedisSerializer; 来序列化
            FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
            redisTemplate.setValueSerializer(fastJsonRedisSerializer);
            redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
         */
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域：field、get 和 set,以及修饰符范围：ANY是都要，包括 private 和 public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非 final 修饰的
        // 序列化时将对象全类名一起保存下来
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 设置时间的格式化
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 注入到 factory 工厂中
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 将操作 string redis 类型的 ValueOperations 对象注册到容器
     *
     * @param redisTemplate RedisTemplate 对象
     * @return ValueOperations 对象
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 将操作 list redis 类型的 ListOperations 对象注册到容器
     *
     * @param redisTemplate RedisTemplate 对象
     * @return ListOperations 对象
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 将操作 set redis 类型的 SetOperations 对象注册到容器
     *
     * @param redisTemplate RedisTemplate 对象
     * @return SetOperations 对象
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 将操作 zSet redis 类型的 ZSetOperations 对象注册到容器
     *
     * @param redisTemplate RedisTemplate 对象
     * @return ZSetOperations 对象
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * 将操作 hash redis 类型的 ZSetOperations 对象注册到容器
     *
     * @param redisTemplate RedisTemplate 对象
     * @return HashOperations 对象
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }
}
