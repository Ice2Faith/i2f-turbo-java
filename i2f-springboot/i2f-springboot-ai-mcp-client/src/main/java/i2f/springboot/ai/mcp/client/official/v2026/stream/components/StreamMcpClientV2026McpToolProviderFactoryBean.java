package i2f.springboot.ai.mcp.client.official.v2026.stream.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.net.http.data.HttpHeaders;
import i2f.spring.web.rest.SpringWebRestClient;
import i2f.springboot.ai.mcp.client.official.v2026.stream.properties.StreamMcpClientV2026Properties;
import i2f.springboot.ai.mcp.client.official.v2026.stream.provider.StreamJsonRpcMcpClientV2026ToolProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:39
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class StreamMcpClientV2026McpToolProviderFactoryBean implements FactoryBean<McpToolProvider> {
    private StreamMcpClientV2026Properties.InstanceConfig config;
    private ApplicationContext context;

    @Override
    public McpToolProvider getObject() throws Exception {
        StreamJsonRpcMcpClientV2026ToolProvider provider = new StreamJsonRpcMcpClientV2026ToolProvider();
        provider.setBaseUrl(config.getUrl());
        provider.setHeaders(HttpHeaders.create());
        if (config.getBearerToken() != null && !config.getBearerToken().isEmpty()) {
            provider.getHeaders().add("Authorization", "Bearer " + config.getBearerToken());
        }
        if (config.getHeaders() != null) {
            provider.getHeaders().addAll(config.getHeaders());
        }
        provider.setRestClient(new SpringWebRestClient(new RestTemplate()));
        provider.setJsonSerializer(new JacksonJsonSerializer(new ObjectMapper()));
        provider.setName(config.getName());
        provider.setDescription(config.getDescription());
        provider.setTagRules(config.getTagRules());

        Boolean initial = config.getInitial();
        if (initial != null && initial) {
            new Thread(() -> {
                try {
                    List<ToolDefinition> tools = provider.listTools();
                    log.info("initial stream mcp client [" + config.getName() + "] load tools count: " + tools.size());
                } catch (Exception e) {
                    log.warn("initial stream mcp client [" + config.getName() + "] warring: " + e.getMessage(), e);
                }
            }).start();
        }

        return provider;
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
