package i2f.springboot.ai.mcp.server.simple;

import i2f.ai.rest.mcp.simple.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.simple.server.impl.HttpSimpleMcpServerImpl;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.cache.std.expire.IExpireCache;
import i2f.springboot.ai.mcp.server.provider.McpServerProviderAutoConfiguration;
import i2f.springboot.ai.mcp.server.simple.properties.HttpSimpleMcpServerProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/7/21 15:37
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.simple.enable:true}")
@Configuration
@AutoConfigureAfter(McpServerProviderAutoConfiguration.class)
@EnableConfigurationProperties({
        HttpSimpleMcpServerProperties.class
})
@Slf4j
@Data
public class SimpleMcpServerAutoConfiguration implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    protected HttpSimpleMcpServerProperties httpSimpleMcpServerProperties;

    @Autowired(required = false)
    protected IExpireCache<String, Object> expireCache;

    @Autowired
    protected McpServerProvider mcpServerProvider;

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.simple.server.enable:true}")
    @ConditionalOnMissingBean(HttpSimpleMcpServer.class)
    @Bean
    public HttpSimpleMcpServer httpSimpleMcpServer() {
        HttpSimpleMcpServerImpl ret = new HttpSimpleMcpServerImpl().toMutator()
                .set(u -> u::setMcpServerProvider, mcpServerProvider)
                .set(u -> u::setExpireWindowMinutes, httpSimpleMcpServerProperties.getExpireWindowMinutes())
                .set(u -> u::setExpireCache, expireCache)
                .apply(u -> {
                    if (httpSimpleMcpServerProperties.getAppList() != null) {
                        u.getAppList().addAll(httpSimpleMcpServerProperties.getAppList());
                    }
                })
                .set(u -> u::setHmacName, httpSimpleMcpServerProperties.getHmacName())
                .done();
        return ret;
    }


}
