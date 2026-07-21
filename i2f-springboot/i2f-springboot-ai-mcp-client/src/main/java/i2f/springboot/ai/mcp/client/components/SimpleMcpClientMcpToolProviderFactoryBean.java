package i2f.springboot.ai.mcp.client.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.client.HttpSimpleMcpClientToolProvider;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.web.rest.SpringWebRestClient;
import i2f.springboot.ai.mcp.client.properties.SimpleMcpClientProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:39
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleMcpClientMcpToolProviderFactoryBean implements FactoryBean<McpToolProvider> {
    private SimpleMcpClientProperties.InstanceConfig config;
    private ApplicationContext context;

    @Override
    public McpToolProvider getObject() throws Exception {
        HttpSimpleMcpClientToolProvider ret = new HttpSimpleMcpClientToolProvider().toMutator()
                .set(u -> u::setRestClient, new SpringWebRestClient(new RestTemplate()))
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .set(u -> u::setBaseUrl, config.getBaseUrl())
                .set(u -> u::setAppId, config.getAppId())
                .set(u -> u::setAppKey, config.getAppKey())
                .set(u -> u::setHmacName, config.getHmacName())
                .set(u -> u::setName, config.getName())
                .set(u -> u::setDescription, config.getDescription())
                .done();
        return ret;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return McpToolProvider.class;
    }
}
