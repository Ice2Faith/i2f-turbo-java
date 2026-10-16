package i2f.springboot.ai.mcp.client.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.resources.ResourceUtil;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.springboot.ai.mcp.client.creator.components.McpClientCreatorMcpToolProviderFactoryBean;
import i2f.springboot.ai.mcp.client.creator.data.McpServerProperties;
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
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.client.creator.enable:true}")
@Configuration
@EnableConfigurationProperties({
        McpServerProperties.class
})
@Slf4j
@Data
public class McpClientCreatorAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    public static final String MCP_JSON_PATH = "mcp/mcp.json";
    protected ApplicationContext applicationContext;

    public static McpServerProperties readMcpJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File("./" + MCP_JSON_PATH);
        if (file.exists()) {
            McpServerProperties ret = mapper.readValue(file, McpServerProperties.class);
            if (ret != null) {
                if (ret.getMcpServers() != null && !ret.getMcpServers().isEmpty()) {
                    return ret;
                }
                if (ret.getServers() != null && !ret.getServers().isEmpty()) {
                    return ret;
                }
            }
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            McpServerProperties prop = new McpServerProperties();
            prop.setMcpServers(new HashMap<>());
            mapper.writeValue(file, prop);
        }

        InputStream is = ResourceUtil.getClasspathResourceAsStream(MCP_JSON_PATH);
        if (is != null) {
            McpServerProperties ret = mapper.readValue(is, McpServerProperties.class);
            if (ret != null) {
                if (ret.getMcpServers() != null && !ret.getMcpServers().isEmpty()) {
                    return ret;
                }
                if (ret.getServers() != null && !ret.getServers().isEmpty()) {
                    return ret;
                }
            }
        }
        return null;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Binder binder = Binder.get(applicationContext.getEnvironment());
        McpServerProperties proxyProperties = binder.bind(McpServerProperties.CONFIG_PREFIX, McpServerProperties.class).orElseGet(McpServerProperties::new);
        try {
            McpServerCreator creator = new McpServerCreator() {
                @Override
                public IJsonSerializer jsonSerializer() {
                    return new JacksonJsonSerializer(new ObjectMapper());
                }

                @Override
                public String variableReplacer(String variable) {
                    String prop = applicationContext.getEnvironment().getProperty(variable);
                    if (prop != null && !prop.isEmpty()) {
                        return prop;
                    }
                    return super.variableReplacer(variable);
                }

                @Override
                public void logInfo(String message, Throwable e) {
                    if (e != null) {
                        log.info(message, e);
                    } else {
                        log.info(message);
                    }
                }

                @Override
                public void logWarn(String message, Throwable e) {
                    if (e != null) {
                        log.warn(message, e);
                    } else {
                        log.warn(message);
                    }
                }

                @Override
                public void logError(String message, Throwable e) {
                    if (e != null) {
                        log.error(message, e);
                    } else {
                        log.error(message);
                    }
                }
            };
            try {
                McpServerProperties jsonProp = readMcpJson();
                if (jsonProp != null) {
                    if (jsonProp.getMcpServers() != null) {
                        if (proxyProperties.getMcpServers() == null) {
                            proxyProperties.setMcpServers(new HashMap<>());
                        }
                        proxyProperties.getMcpServers().putAll(jsonProp.getMcpServers());
                    }
                    if (jsonProp.getServers() != null) {
                        if (proxyProperties.getServers() == null) {
                            proxyProperties.setServers(new HashMap<>());
                        }
                        proxyProperties.getServers().putAll(jsonProp.getServers());
                    }
                }
            } catch (Exception e) {
                // ignore
                log.warn("read `" + MCP_JSON_PATH + "` failure, jump!");
            }
            List<McpToolProvider> providers = creator.create(proxyProperties);
            for (McpToolProvider provider : providers) {
                String name = provider.getName();

                name = name.replace("-", "_");
                String beanName = name + "_McpToolProvider";

                GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(McpToolProvider.class)
                        .getRawBeanDefinition();
                definition.getPropertyValues().add("provider", provider);
                definition.getPropertyValues().add("context", applicationContext);
                definition.setBeanClass(McpClientCreatorMcpToolProviderFactoryBean.class);
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
