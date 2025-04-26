package i2f.spring.ai.chat.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/4/26 15:22
 * @desc
 */
@ConfigurationProperties(prefix = "i2f.ai.chat")
@Data
@NoArgsConstructor
public class ChatAiProperties {
    protected String defaultSystem;
    protected String defaultSystemResource;
    protected boolean allowAutoCreateSessionId = true;
    protected boolean allowCookieSessionId = false;
    protected boolean supportTools = false;
    protected List<String> toolClasses;
}
