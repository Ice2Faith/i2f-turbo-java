package i2f.springboot.ops.openai.tts;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/7/27 11:20
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "ai.tts.qwen")
public class QwenTtsProperties {
    protected String url;
    protected String apiKey;
    protected String model;
    protected String voice;
}
