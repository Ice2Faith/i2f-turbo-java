package i2f.springboot.ai.mcp.client.solon.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.springboot.ai.mcp.client.solon.properties.SolonMcpClientProperties;
import i2f.springboot.ai.mcp.client.solon.provider.SolonMcpToolProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/7 16:39
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class SolonMcpClientMcpToolProviderFactoryBean implements FactoryBean<McpToolProvider> {
    private SolonMcpClientProperties.InstanceConfig config;
    private ApplicationContext context;

    @Override
    public McpToolProvider getObject() throws Exception {
        SolonMcpClientProperties.Channel channel = config.getChannel();
        if (channel == null) {
            channel = SolonMcpClientProperties.Channel.STREAMABLE;
        }
        McpClientProvider.Builder builder = McpClientProvider.builder()
                .channel(channel.channel())
                .url(config.getUrl())
                .cacheSeconds(30);
        if (config.getBearerToken() != null && !config.getBearerToken().isEmpty()) {
            builder.header("Authorization", "Bearer " + config.getBearerToken());
        }
        Map<String, Object> headers = config.getHeaders();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                builder.header(entry.getKey(), String.valueOf(value));
            }
        }

        McpClientProvider mcpClient = builder.build();

        SolonMcpToolProvider provider = new SolonMcpToolProvider();
        provider.setMcpClient(mcpClient);
        provider.setJsonSerializer(new JacksonJsonSerializer(new ObjectMapper()));
        provider.setName(config.getName());
        provider.setDescription(config.getDescription());

        Boolean initial = config.getInitial();
        if (initial != null && initial) {
            new Thread(() -> {
                try {
                    List<ToolDefinition> tools = provider.getTools();
                    log.info("initial solon mcp client [" + config.getName() + "] load tools count: " + tools.size());
                } catch (Exception e) {
                    log.warn("initial solon mcp client [" + config.getName() + "] warring: " + e.getMessage(), e);
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
