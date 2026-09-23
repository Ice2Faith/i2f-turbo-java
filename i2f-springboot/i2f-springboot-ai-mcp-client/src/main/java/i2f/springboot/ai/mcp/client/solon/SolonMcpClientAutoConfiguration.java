package i2f.springboot.ai.mcp.client.solon;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.springboot.ai.mcp.client.simple.properties.SimpleMcpClientProperties;
import i2f.springboot.ai.mcp.client.solon.components.SolonMcpClientMcpToolProviderFactoryBean;
import i2f.springboot.ai.mcp.client.solon.properties.SolonMcpClientProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import reactor.util.context.ContextView;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.client.solon.enable:true}")
@ConditionalOnClass({
        McpClientProvider.class,
        ContextView.class
})
@Configuration
@EnableConfigurationProperties({
        SolonMcpClientProperties.class
})
@Slf4j
@Data
public class SolonMcpClientAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    protected ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Binder binder = Binder.get(applicationContext.getEnvironment());
        SolonMcpClientProperties proxyProperties = binder.bind(SolonMcpClientProperties.CONFIG_PREFIX, SolonMcpClientProperties.class).orElseGet(SolonMcpClientProperties::new);
        try {
            List<SolonMcpClientProperties.InstanceConfig> instances = proxyProperties.getInstances();
            if (instances == null) {
                return;
            }
            for (SolonMcpClientProperties.InstanceConfig config : instances) {
                Boolean enable = config.getEnable();
                if (enable != null && !enable) {
                    continue;
                }
                String name = config.getName();
                if (name == null || name.isEmpty()) {
                    log.warn("registry mcp ignore instance which is blank name.");
                    continue;
                }

                name = name.replace("-", "_");
                String beanName = name + "_McpToolProvider";

                GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(McpToolProvider.class)
                        .getRawBeanDefinition();
                definition.getPropertyValues().add("config", config);
                definition.getPropertyValues().add("context", applicationContext);
                definition.setBeanClass(SolonMcpClientMcpToolProviderFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setLazyInit(true);
                beanDefinitionRegistry.registerBeanDefinition(beanName, definition);

                log.info("registry mcp provider bean " + beanName + " of name " + name);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
