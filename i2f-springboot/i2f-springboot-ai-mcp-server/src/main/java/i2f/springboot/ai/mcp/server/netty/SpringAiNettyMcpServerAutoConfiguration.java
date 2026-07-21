package i2f.springboot.ai.mcp.server.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.springboot.ai.mcp.server.SpringAiMcpServerAutoConfiguration;
import i2f.springboot.ai.mcp.server.netty.impl.HttpSimpleMcpInBoundHandler;
import i2f.springboot.ai.mcp.server.netty.impl.NettyHttpSimpleMcpServer;
import i2f.springboot.ai.mcp.server.netty.properties.NettySimpleMcpServerProperties;
import io.netty.bootstrap.ServerBootstrap;
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

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.netty.enable:false}")
@ConditionalOnClass(ServerBootstrap.class)
@AutoConfigureAfter(SpringAiMcpServerAutoConfiguration.class)
@EnableConfigurationProperties({
        NettySimpleMcpServerProperties.class
})
@Configuration
@Slf4j
@Data
public class SpringAiNettyMcpServerAutoConfiguration {

    @Autowired
    protected NettySimpleMcpServerProperties nettySimpleMcpServerProperties;

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.netty.handler.enable:true}")
    @ConditionalOnMissingBean(HttpSimpleMcpInBoundHandler.class)
    @Bean
    public HttpSimpleMcpInBoundHandler httpSimpleMcpInBoundHandler(@Autowired HttpSimpleMcpServer httpSimpleMcpServer) {
        return new HttpSimpleMcpInBoundHandler().toMutator()
                .set(u -> u::setServer, httpSimpleMcpServer)
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .done();
    }

    @ConditionalOnExpression("${i2f.springboot.ai.mcp.server.netty.server.enable:true}")
    @ConditionalOnMissingBean(NettyHttpSimpleMcpServer.class)
    @Bean
    public NettyHttpSimpleMcpServer nettyHttpSimpleMcpServer(@Autowired HttpSimpleMcpInBoundHandler httpSimpleMcpInBoundHandler) {
        NettyHttpSimpleMcpServer server = new NettyHttpSimpleMcpServer().toMutator()
                .set(u -> u::setHandler, httpSimpleMcpInBoundHandler)
                .set(u -> u::setPort, nettySimpleMcpServerProperties.getPort())
                .set(u -> u::setBossThread, nettySimpleMcpServerProperties.getBossThread())
                .set(u -> u::setWorkerThread, nettySimpleMcpServerProperties.getWorkerThread())
                .set(u -> u::setMaxContentLength, nettySimpleMcpServerProperties.getMaxContentLength())
                .done();
        Thread thread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        });
        thread.setName("netty-mcp-server");
        thread.setDaemon(true);
        thread.start();
        return server;
    }
}
