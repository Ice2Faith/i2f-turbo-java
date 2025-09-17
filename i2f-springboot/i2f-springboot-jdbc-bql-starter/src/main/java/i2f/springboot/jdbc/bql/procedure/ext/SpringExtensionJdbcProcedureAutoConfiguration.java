package i2f.springboot.jdbc.bql.procedure.ext;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.jdbc.procedure.context.ContextHolder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ice2Faith
 * @date 2025/9/17 8:40
 */
@Configuration
public class SpringExtensionJdbcProcedureAutoConfiguration implements InitializingBean {
    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private ObjectMapper objectMapper;
    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringEnvironmentFunctions.environment = environment;
        ContextHolder.registryAllInvokeMethods(SpringContextFunctions.class);

        SpringContextFunctions.applicationContext = applicationContext;
        ContextHolder.registryAllInvokeMethods(SpringContextFunctions.class);

        SpringWebFunctions.objectMapper = objectMapper;
        SpringWebFunctions.restTemplate = restTemplate;
        ContextHolder.registryAllInvokeMethods(SpringWebFunctions.class);

        if (redisTemplate != null) {
            SpringRedisFunctions.redisTemplate = redisTemplate;
            ContextHolder.registryAllInvokeMethods(SpringRedisFunctions.class);
        }

        if (redissonClient != null) {
            SpringRedissonFunctions.redissonClient = redissonClient;
            ContextHolder.registryAllInvokeMethods(SpringRedissonFunctions.class);
        }
    }
}
