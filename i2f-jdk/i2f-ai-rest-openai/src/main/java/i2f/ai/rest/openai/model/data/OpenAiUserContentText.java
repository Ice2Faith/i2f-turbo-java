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
public class OpenAiUserContentText implements OpenAiUserContent {
    protected String type=OpenAiConsts.TEXT;
    protected String text;

    public OpenAiUserContentText(String text) {
        this.text = text;
    }

    @Override
    public String type() {
        return type;
    }
}
