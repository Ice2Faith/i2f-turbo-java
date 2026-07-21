package i2f.springboot.ai.mcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.server.impl.HttpSimpleMcpServerImpl;
import i2f.cache.std.expire.IExpireCache;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.spring.core.SpringContext;
import i2f.springboot.ai.mcp.server.properties.HttpSimpleMcpServerProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.enable:true}")
@Configuration
@EnableConfigurationProperties({
        HttpSimpleMcpServerProperties.class
})
@Slf4j
@Data
public class SpringAiMcpServerAutoConfiguration implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    protected HttpSimpleMcpServerProperties httpSimpleMcpServerProperties;

    @Autowired(required = false)
    protected IExpireCache<String, Object> expireCache;

    @Autowired(required = false)
    protected IProxyInvocationHandler invocationHandler;

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.simple-server.enable:true}")
    @ConditionalOnMissingBean(HttpSimpleMcpServer.class)
    @Bean
    public HttpSimpleMcpServer httpSimpleMcpServer() {
        HttpSimpleMcpServerImpl ret = new HttpSimpleMcpServerImpl().toMutator()
                .set(u -> u::setContext, new SpringContext())
                .set(u -> u::setExpireWindowMinutes, httpSimpleMcpServerProperties.getExpireWindowMinutes())
                .set(u -> u::setExpireCache, expireCache)
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .set(u -> u::setInvocationHandler, invocationHandler)
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
