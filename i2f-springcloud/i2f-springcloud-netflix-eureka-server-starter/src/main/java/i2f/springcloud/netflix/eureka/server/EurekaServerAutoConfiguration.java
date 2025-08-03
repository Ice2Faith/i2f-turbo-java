package i2f.springcloud.netflix.eureka.server;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2022/6/12 20:37
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.eureka-server.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.eureka-server")
@Configuration
@EnableEurekaServer
public class EurekaServerAutoConfiguration {

}
