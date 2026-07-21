package i2f.springboot.ai.mcp.client;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.springboot.ai.mcp.client.components.SimpleMcpClientMcpToolProviderFactoryBean;
import i2f.springboot.ai.mcp.client.properties.SimpleMcpClientProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.client.enable:true}")
@Configuration
@EnableConfigurationProperties({
        SimpleMcpClientProperties.class
})
@Slf4j
@Data
public class SpringAiMcpClientAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    protected ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        SimpleMcpClientProperties proxyProperties = applicationContext.getBean(SimpleMcpClientProperties.class);
        try {
            List<SimpleMcpClientProperties.InstanceConfig> instances = proxyProperties.getInstances();
            if (instances == null) {
                return;
            }
            for (SimpleMcpClientProperties.InstanceConfig config : instances) {
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
                definition.setBeanClass(SimpleMcpClientMcpToolProviderFactoryBean.class);
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
