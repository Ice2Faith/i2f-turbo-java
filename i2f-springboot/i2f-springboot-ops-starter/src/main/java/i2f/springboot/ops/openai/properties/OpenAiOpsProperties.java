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

    protected VisionOptions vision=new VisionOptions();

    @Data
    @NoArgsConstructor
    public static class VisionOptions{
        protected int imageMaxSizeKb=256;
        protected int imageMaxDimension=960;
    }

}
