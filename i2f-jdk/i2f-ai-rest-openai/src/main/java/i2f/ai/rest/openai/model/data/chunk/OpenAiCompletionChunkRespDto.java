package i2f.ai.rest.openai.model.data.chunk;

import i2f.ai.rest.openai.model.data.OpenAiAssistantMessageRespDto;
import i2f.ai.rest.openai.model.data.OpenAiCompletionChoice;
import i2f.ai.rest.openai.model.data.OpenAiCompletionUsage;
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
public class OpenAiCompletionChunkRespDto {
    protected String id;
    protected String object;
    protected Long created;
    protected String model;
    protected List<OpenAiCompletionChoice> choices;
    protected OpenAiCompletionUsage usage;
    protected String system_fingerprint;

    public OpenAiAssistantMessageRespDto getFirstMessage() {
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        OpenAiCompletionChoice choice = choices.get(0);
        return choice.getMessage();
    }
}
