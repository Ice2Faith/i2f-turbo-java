package i2f.springboot.ai.mcp.server.official.v2024.stream.springweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.mcp.server.official.auth.StreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.official.auth.impl.StaticStreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.official.v2024.stream.properties.OfficialMcpServerProperties;
import i2f.springboot.ai.mcp.server.official.v2024.stream.springweb.impl.SpringHttpStreamMcpController;
import i2f.springboot.ai.mcp.server.provider.McpServerProviderAutoConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

/**
 * 官方 MCP 协议（protocolVersion: 2024-11-05）Streamable HTTP 传输的 SpringWeb 服务端自动配置。
 * <p>
 * 区别于 {@code SimpleSpringWebMcpServerAutoConfiguration}（私有 simple 协议，带 HMAC 签名验证），
 * 本配置直接桥接 {@link SpringContext} + {@link i2f.ai.std.tool.ToolRawHelper}，
 * 不依赖 HttpSimpleMcpServer，不做请求头验签。
 *
 * @author Ice2Faith
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.official.v2024.stream.springweb.enable:true}")
@ConditionalOnClass(RestController.class)
@AutoConfigureAfter(McpServerProviderAutoConfiguration.class)
@EnableConfigurationProperties({
        OfficialMcpServerProperties.class
})
@Configuration
@Slf4j
@Data
public class StreamSpringWebMcpServerAutoConfiguration {

    @Autowired
    private OfficialMcpServerProperties officialMcpServerProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @ConditionalOnMissingBean(StreamMcpServerAuthFilter.class)
    @Bean
    public StreamMcpServerAuthFilter streamMcpServerAuthFilter() {
        StaticStreamMcpServerAuthFilter ret = new StaticStreamMcpServerAuthFilter();
        OfficialMcpServerProperties.BearerTokenOptions bearerToken = officialMcpServerProperties.getBearerToken();
        if(bearerToken!=null) {
            ret.setEnable(officialMcpServerProperties.getBearerToken().isEnable());
            List<String> allowTokens = officialMcpServerProperties.getBearerToken().getAllowTokens();
            if (allowTokens != null) {
                ret.setAllowBearerTokens(new HashSet<>(allowTokens));
            }
        }
        return ret;
    }

    @ConditionalOnMissingBean(SpringHttpStreamMcpController.class)
    @Bean
    public SpringHttpStreamMcpController springHttpStreamMcpController(@Autowired McpServerProvider mcpServerProvider,
                                                                       @Autowired(required = false) StreamMcpServerAuthFilter streamMcpServerAuthFilter) {
        return new SpringHttpStreamMcpController().toMutator()
                .set(u -> u::setProperties, officialMcpServerProperties)
                .set(u -> u::setMcpServerProvider, mcpServerProvider)
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .set(u -> u::setStreamMcpServerAuthFilter, streamMcpServerAuthFilter)
                .done();
    }
}
