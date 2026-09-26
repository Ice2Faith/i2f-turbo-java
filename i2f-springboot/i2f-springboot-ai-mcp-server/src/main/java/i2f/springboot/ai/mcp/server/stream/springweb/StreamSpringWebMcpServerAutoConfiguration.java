package i2f.springboot.ai.mcp.server.stream.springweb;

import i2f.proxy.std.IProxyInvocationHandler;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.mcp.server.stream.auth.StreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.stream.auth.impl.StaticStreamMcpServerAuthFilter;
import i2f.springboot.ai.mcp.server.stream.properties.OfficialMcpServerProperties;
import i2f.springboot.ai.mcp.server.stream.springweb.impl.SpringHttpStreamMcpController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

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
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.stream.springweb.enable:true}")
@ConditionalOnClass(RestController.class)
@EnableConfigurationProperties({
        OfficialMcpServerProperties.class
})
@Configuration
@Slf4j
@Data
public class StreamSpringWebMcpServerAutoConfiguration {

    @Autowired
    private OfficialMcpServerProperties officialMcpServerProperties;

    @ConditionalOnMissingBean(StreamMcpServerAuthFilter.class)
    @Bean
    public StreamMcpServerAuthFilter streamMcpServerAuthFilter() {
        StaticStreamMcpServerAuthFilter ret = new StaticStreamMcpServerAuthFilter();
        ret.setEnable(officialMcpServerProperties.getBearerToken().isEnable());
        ret.setAllowBearerTokens(new HashSet<>(officialMcpServerProperties.getBearerToken().getAllowTokens()));
        return ret;
    }

    @ConditionalOnMissingBean(SpringHttpStreamMcpController.class)
    @Bean
    public SpringHttpStreamMcpController springHttpStreamMcpController(@Autowired ApplicationContext applicationContext,
                                                                       @Autowired(required = false) IProxyInvocationHandler invocationHandler,
                                                                       @Autowired(required = false) StreamMcpServerAuthFilter streamMcpServerAuthFilter) {
        return new SpringHttpStreamMcpController().toMutator()
                .set(u -> u::setProperties, officialMcpServerProperties)
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setInvocationHandler, invocationHandler)
                .set(u -> u::setStreamMcpServerAuthFilter, streamMcpServerAuthFilter)
                .done();
    }
}
