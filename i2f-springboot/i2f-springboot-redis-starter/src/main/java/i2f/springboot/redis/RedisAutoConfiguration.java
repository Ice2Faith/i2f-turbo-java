package i2f.springboot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Ice2Faith
 * @date 2022/4/12 14:05
 * @desc
 */
@ConditionalOnExpression("${i2f.spring.redis.enable:true}")
@Slf4j
@ConfigurationProperties(prefix = "i2f.spring.redis")
@EnableRedisRepositories
public class RedisAutoConfiguration {

    String dateFormat = "yyyy-MM-dd HH:mm:ss SSS";

    @ConditionalOnMissingBean(JacksonJsonRedisSerializer.class)
    @Bean
    public JacksonJsonRedisSerializer<Object> getRedisSerializer() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .defaultLocale(Locale.getDefault())
                .defaultDateFormat(new SimpleDateFormat(dateFormat))
                .build();
        JacksonJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new JacksonJsonRedisSerializer<>(objectMapper, Object.class);
        log.info("Jackson2JsonRedisSerializer config done.");
        return jackson2JsonRedisSerializer;
    }

}
