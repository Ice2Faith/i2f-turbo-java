package i2f.springcloud.discovery;

import i2f.springcloud.discovery.properties.DiscoveryProperties;
import i2f.springcloud.discovery.provider.DiscoveryClientProvider;
import i2f.springcloud.discovery.provider.DiscoveryServiceInstance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.noop.NoopDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/6/27 16:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.discovery.enable:true}")
@Data
@NoArgsConstructor
@Slf4j
@Configuration(
        proxyBeanMethods = false
)
@AutoConfigureBefore({
        NoopDiscoveryClientAutoConfiguration.class,
        CommonsClientAutoConfiguration.class,
        SimpleDiscoveryClientAutoConfiguration.class
})
@Import({
        DiscoveryProperties.class
})
public class DiscoveryAutoConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    public static final int DEFAULT_APPLICATION_PORT = 8080;

    private DiscoveryServiceInstance local = new DiscoveryServiceInstance();

    @Autowired(required = false)
    private ServerProperties server;

    @Autowired
    private InetUtils inet;

    @Autowired
    private DiscoveryProperties properties;

    @Autowired
    private Environment environment;

    @Bean
    @Order(10)
    public DiscoveryClient discoveryClientProvider() {
        DiscoveryClientProvider provider = new DiscoveryClientProvider();
        provider.setOrder(properties.getOrder());
        Map<String, List<DiscoveryServiceInstance>> instances = properties.getInstances();
        if (instances != null) {
            for (Map.Entry<String, List<DiscoveryServiceInstance>> entry : instances.entrySet()) {
                String key = entry.getKey();
                List<DiscoveryServiceInstance> list = entry.getValue();
                if (list == null) {
                    continue;
                }
                for (DiscoveryServiceInstance item : list) {
                    item.setServiceId(key);
                }
                provider.getHolder().put(key, new CopyOnWriteArrayList<>(list));
                log.info("discovery config : " + key);
            }
        }

        if (provider.getHolder().isEmpty()) {
            log.warn("discovery empty, please config client like : \n" +
                    "i2f:\n" +
                    "  springcloud:\n" +
                    "    discovery:\n" +
                    "      enable: true\n" +
                    "      order: 0\n" +
                    "      instances:\n" +
                    "        sys-app:\n" +
                    "          - uri: http://192.168.1.100:8080/\n" +
                    "          - uri: http://192.168.1.101:8080/\n" +
                    "        file-svc:\n" +
                    "          - uri: http://192.168.1.110:8080/");
        }

        String serviceId = environment.getProperty("spring.application.name", "application");
        local.setServiceId(serviceId);
        local.setHost(this.inet.findFirstNonLoopbackHostInfo().getHostname());
        local.setPort(this.findPort());

        provider.getHolder().put(local.getServiceId(), new CopyOnWriteArrayList<>(Collections.singletonList(local)));
        log.info("discovery config : " + local.getServiceId());
        return provider;
    }

    private int findPort() {
        if (server == null) {
            return DEFAULT_APPLICATION_PORT;
        }
        Integer port = server.getPort();
        if (port == null) {
            return DEFAULT_APPLICATION_PORT;
        }
        if (port <= 0) {
            return DEFAULT_APPLICATION_PORT;
        }
        return port;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        if (port > 0) {
            local.setPort(port);
        }
    }
}
