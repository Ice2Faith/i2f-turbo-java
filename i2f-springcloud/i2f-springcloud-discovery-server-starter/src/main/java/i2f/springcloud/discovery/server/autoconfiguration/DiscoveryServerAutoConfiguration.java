package i2f.springcloud.discovery.server.autoconfiguration;

import i2f.springcloud.discovery.server.controller.DiscoverServerController;
import i2f.springcloud.discovery.server.manager.impl.RedisServiceInstanceManager;
import i2f.springcloud.discovery.server.properties.DiscoveryServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Ice2Faith
 * @date 2025/9/25 16:51
 */
@ConditionalOnExpression("${i2f.springcloud.discovery.server.enable:true}")
@Slf4j
@Import({
        RedisServiceInstanceManager.class,
        DiscoverServerController.class
})
@EnableConfigurationProperties(DiscoveryServerProperties.class)
@Configuration
public class DiscoveryServerAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DiscoveryServerAutoConfiguration config.");
    }
}
