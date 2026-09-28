package i2f.springboot.ai.mcp.server.official.v2026.stream.springweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.springboot.ai.mcp.server.official.auth.StreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.official.auth.impl.StaticStreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.official.v2026.stream.properties.OfficialMcpServerV2026Properties;
import i2f.springboot.ai.mcp.server.official.v2026.stream.springweb.impl.SpringHttpStreamMcpV2026Controller;
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
 * 官方 MCP 协议（protocolVersion: 2026-07-28 无状态版本）Streamable HTTP 传输的 SpringWeb 服务端自动配置。
 * <p>
 * 与 {@code v2024} 的 {@code StreamSpringWebMcpServerAutoConfiguration} 相对，本实现不再装配
 * 可插拔的鉴权过滤器：Bearer Token 校验由 {@link SpringHttpStreamMcpV2026Controller} 直接依据
 * {@link OfficialMcpServerV2026Properties} 内联完成，以保持 2026 无状态实现的自包含。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP Streamable HTTP 服务端自动配置
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.official.v2026.stream.springweb.enable:true}")
@ConditionalOnClass(RestController.class)
@AutoConfigureAfter(McpServerProviderAutoConfiguration.class)
@EnableConfigurationProperties({
        OfficialMcpServerV2026Properties.class
})
@Configuration
@Slf4j
@Data
public class StreamSpringWebMcpServerV2026AutoConfiguration {

    @Autowired
    private OfficialMcpServerV2026Properties officialMcpServerV2026Properties;

    @Autowired
    private ObjectMapper objectMapper;

    @ConditionalOnMissingBean(StreamMcpServerAuthFilter.class)
    @Bean
    public StreamMcpServerAuthFilter streamMcpServerAuthFilter() {
        StaticStreamMcpServerAuthFilter ret = new StaticStreamMcpServerAuthFilter();
        OfficialMcpServerV2026Properties.BearerTokenOptions bearerToken = officialMcpServerV2026Properties.getBearerToken();
        if(bearerToken!=null) {
            ret.setEnable(officialMcpServerV2026Properties.getBearerToken().isEnable());
            List<String> allowTokens = officialMcpServerV2026Properties.getBearerToken().getAllowTokens();
            if (allowTokens != null) {
                ret.setAllowBearerTokens(new HashSet<>(allowTokens));
            }
        }
        return ret;
    }

    @ConditionalOnMissingBean(SpringHttpStreamMcpV2026Controller.class)
    @Bean
    public SpringHttpStreamMcpV2026Controller springHttpStreamMcpV2026Controller(
            @Autowired McpServerProvider mcpServerProvider,
            @Autowired(required = false) StreamMcpServerAuthFilter streamMcpServerAuthFilter) {
        return new SpringHttpStreamMcpV2026Controller().toMutator()
                .set(u -> u::setProperties, officialMcpServerV2026Properties)
                .set(u -> u::setMcpServerProvider, mcpServerProvider)
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(objectMapper))
                .set(u -> u::setStreamMcpServerAuthFilter, streamMcpServerAuthFilter)
                .done();
    }
}
