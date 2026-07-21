package i2f.springboot.ai.mcp.server.springweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.springboot.ai.mcp.server.SpringAiMcpServerAutoConfiguration;
import i2f.springboot.ai.mcp.server.springweb.impl.SpringHttpSimpleMcpController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.server.springweb.enable:true}")
@ConditionalOnClass(RestController.class)
@AutoConfigureAfter(SpringAiMcpServerAutoConfiguration.class)
@Configuration
@Slf4j
@Data
public class SpringAiSpringWebMcpServerAutoConfiguration {

    @ConditionalOnMissingBean(SpringHttpSimpleMcpController.class)
    @Bean
    public SpringHttpSimpleMcpController springHttpSimpleMcpController(@Autowired HttpSimpleMcpServer httpSimpleMcpServer) {
        return new SpringHttpSimpleMcpController().toMutator()
                .set(u -> u::setServer, httpSimpleMcpServer)
                .set(u -> u::setJsonSerializer, new JacksonJsonSerializer(new ObjectMapper()))
                .done();
    }
}
