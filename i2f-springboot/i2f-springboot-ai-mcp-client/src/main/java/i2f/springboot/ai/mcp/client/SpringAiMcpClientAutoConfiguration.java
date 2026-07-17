package i2f.springboot.ai.mcp.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2026/7/17 21:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ai.mcp.client.enable:true}")
@Configuration
@Slf4j
@Data
public class SpringAiMcpClientAutoConfiguration {
}
