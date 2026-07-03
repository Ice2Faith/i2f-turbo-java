package i2f.ai.rest.openai.model.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/24 21:04
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiCompletionChoice implements BaseBuilder<OpenAiCompletionChoice> {
    protected Integer index;
    protected OpenAiAssistantMessageRespDto message;
    protected Object logprobs;
    protected String finish_reason;
}
