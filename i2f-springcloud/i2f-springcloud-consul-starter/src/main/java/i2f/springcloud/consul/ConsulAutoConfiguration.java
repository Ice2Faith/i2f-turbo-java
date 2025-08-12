package i2f.springcloud.consul;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2025/8/12 18:13
 */
@ConditionalOnExpression("${i2f.springcloud.consul.enable:true}")
@EnableDiscoveryClient
@Configuration
@ConfigurationProperties(prefix = "i2f.springcloud.consul")
public class ConsulAutoConfiguration {

}
