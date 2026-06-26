package i2f.ai.rest.openai.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/6/24 21:04
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiCompletionChoice {
    protected Integer index;
    protected OpenAiAssistantMessageRespDto message;
    protected Object logprobs;
    protected String finish_reason;
}
