package i2f.springboot.ai.mcp.server.official.v2026.stream.springweb;

import i2f.proxy.std.IProxyInvocationHandler;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.mcp.server.official.v2026.stream.properties.OfficialMcpServerV2026Properties;
import i2f.springboot.ai.mcp.server.official.v2026.stream.springweb.impl.SpringHttpStreamMcpV2026Controller;
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
@EnableConfigurationProperties({
        OfficialMcpServerV2026Properties.class
})
@Configuration
@Slf4j
@Data
public class StreamSpringWebMcpServerV2026AutoConfiguration {

    @Autowired
    private OfficialMcpServerV2026Properties officialMcpServerV2026Properties;

    @ConditionalOnMissingBean(SpringHttpStreamMcpV2026Controller.class)
    @Bean
    public SpringHttpStreamMcpV2026Controller springHttpStreamMcpV2026Controller(
            @Autowired ApplicationContext applicationContext,
            @Autowired(required = false) IProxyInvocationHandler invocationHandler) {
        return new SpringHttpStreamMcpV2026Controller().toMutator()
                .set(u -> u::setProperties, officialMcpServerV2026Properties)
                .set(u -> u::setContext, new SpringContext(applicationContext))
                .set(u -> u::setInvocationHandler, invocationHandler)
                .done();
    }
}
