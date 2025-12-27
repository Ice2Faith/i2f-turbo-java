package i2f.spring.ai.mcp.sse.configuration;

import i2f.spring.ai.tool.providers.SpringBeanMethodToolCallbackProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author Ice2Faith
 * @date 2025/6/6 20:15
 * @desc
 */
@Data
@NoArgsConstructor
@Lazy
@Configuration
public class McpAiToolsAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringBeanMethodToolCallbackProvider springBeanMethodToolCallbackProvider() {
        return new SpringBeanMethodToolCallbackProvider(applicationContext);
    }
}
