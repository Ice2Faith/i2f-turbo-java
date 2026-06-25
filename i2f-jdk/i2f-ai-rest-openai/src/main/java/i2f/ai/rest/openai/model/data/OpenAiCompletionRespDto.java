package i2f.ai.rest.openai.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:47
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiCompletionRespDto {
    protected String id;
    protected String object;
    protected Long created;
    protected String model;
    protected List<OpenAiCompletionChoice> choices;
    protected OpenAiCompletionUsage usage;
    protected String system_fingerprint;

    public OpenAiAssistantMessage getFirstMessage() {
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        OpenAiCompletionChoice choice = choices.get(0);
        return choice.getMessage();
    }
}
