package i2f.ai.rest.openai.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/8/6 14:12
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiUserContentImageUrl implements OpenAiUserContent {
    protected String type=OpenAiConsts.IMAGE_URL;
    protected OpenAiImageUrl image_url;

    public OpenAiUserContentImageUrl(OpenAiImageUrl image_url) {
        this.image_url = image_url;
    }

    public OpenAiUserContentImageUrl(String image_url) {
        this.image_url = new OpenAiImageUrl(image_url);
    }

    @Override
    public String type() {
        return type;
    }
}
