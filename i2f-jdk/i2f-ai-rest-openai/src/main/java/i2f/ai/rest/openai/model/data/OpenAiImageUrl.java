package i2f.ai.rest.openai.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/8/6 14:18
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiImageUrl {
    protected String url;

    public OpenAiImageUrl(String url) {
        this.url = url;
    }
}
