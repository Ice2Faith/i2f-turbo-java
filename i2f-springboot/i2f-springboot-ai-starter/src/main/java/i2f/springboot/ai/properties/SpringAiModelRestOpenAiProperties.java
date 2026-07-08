package i2f.springboot.ai.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/3/30 14:31
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ai.model.rest-openai")
public class SpringAiModelRestOpenAiProperties {
    protected String baseUrl;
    protected String apiKey;
    protected String model;
}
