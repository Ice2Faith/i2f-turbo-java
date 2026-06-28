package i2f.ai.rest.openai.model.data.chunk;

import i2f.ai.rest.openai.model.data.OpenAiAssistantMessageRespDto;
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
public class OpenAiCompletionChoiceChunk {
    protected Integer index;
    protected OpenAiAssistantMessageRespDto delta;
    protected Object logprobs;
    protected String finish_reason;
}
