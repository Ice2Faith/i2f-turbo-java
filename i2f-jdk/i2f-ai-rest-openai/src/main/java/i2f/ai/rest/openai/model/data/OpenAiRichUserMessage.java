package i2f.ai.rest.openai.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiRichUserMessage implements OpenAiMessage, BaseMutator<OpenAiRichUserMessage> {
    protected final String role = OpenAiConsts.USER;
    protected List<OpenAiUserContent> content;

    public OpenAiRichUserMessage(List<OpenAiUserContent> content) {
        this.content = content;
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String content() {
        for (OpenAiUserContent item : content) {
            if(item instanceof OpenAiUserContentText){
                OpenAiUserContentText text = (OpenAiUserContentText) item;
                return text.getText();
            }
        }
        return null;
    }
}
