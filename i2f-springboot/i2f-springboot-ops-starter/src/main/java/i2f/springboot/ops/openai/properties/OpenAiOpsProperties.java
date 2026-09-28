package i2f.springboot.ops.openai.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/8/6 15:13
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "ai.openai")
public class OpenAiOpsProperties {

    protected VisionOptions vision = new VisionOptions();

    protected OpenAiOptions defaultEndpoint = new OpenAiOptions();

    protected OpenAiOptions dashscopeEndpoint = new OpenAiOptions();

    @Data
    @NoArgsConstructor
    public static class OpenAiOptions {
        protected boolean enable;
        protected String model;
        protected String baseUrl;
        protected String apiKey;
    }

    @Data
    @NoArgsConstructor
    public static class VisionOptions {
        protected int imageMaxSizeKb = 256;
        protected int imageMaxDimension = 960;
    }

}
