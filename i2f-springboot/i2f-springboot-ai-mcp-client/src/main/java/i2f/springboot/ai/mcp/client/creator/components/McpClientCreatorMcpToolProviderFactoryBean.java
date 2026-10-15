package i2f.springboot.ai.mcp.client.creator.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.net.http.data.HttpHeaders;
import i2f.spring.web.rest.SpringWebRestClient;
import i2f.springboot.ai.mcp.client.official.v2024.stream.properties.StreamMcpClientProperties;
import i2f.springboot.ai.mcp.client.official.v2024.stream.provider.StreamJsonRpcMcpClientToolProvider;
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
public class McpClientCreatorMcpToolProviderFactoryBean implements FactoryBean<McpToolProvider> {
    private McpToolProvider provider;
    private ApplicationContext context;

    @Override
    public McpToolProvider getObject() throws Exception {
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
