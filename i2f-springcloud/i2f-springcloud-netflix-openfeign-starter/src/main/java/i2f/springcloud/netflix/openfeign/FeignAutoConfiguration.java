package i2f.springcloud.netflix.openfeign;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/3/27 14:06
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.feign.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableFeignClients
@Configuration
@ConfigurationProperties(prefix = "i2f.springcloud.feign")
public class FeignAutoConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("FeignConfig config done.");
    }
}
