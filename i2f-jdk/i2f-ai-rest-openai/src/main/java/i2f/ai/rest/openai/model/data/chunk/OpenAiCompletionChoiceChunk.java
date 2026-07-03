package i2f.ai.rest.openai.model.data.chunk;

import i2f.ai.rest.openai.model.data.OpenAiAssistantMessageRespDto;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/24 21:04
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiCompletionChoiceChunk implements BaseMutator<OpenAiCompletionChoiceChunk> {
    protected Integer index;
    protected OpenAiAssistantMessageRespDto delta;
    protected Object logprobs;
    protected String finish_reason;
}
