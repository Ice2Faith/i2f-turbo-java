package i2f.springcloud.netflix.eureka.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2022/6/12 20:37
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.eureka-client.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.eureka-client")
@Configuration
public class EurekaClientAutoConfiguration {

}
