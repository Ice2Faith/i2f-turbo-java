package i2f.ai.rest.openai.model.data.chunk;

import i2f.ai.rest.openai.model.data.OpenAiCompletionUsage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:47
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiCompletionChunkRespDto {
    protected String id;
    protected String object;
    protected Long created;
    protected String model;
    protected List<OpenAiCompletionChoiceChunk> choices;
    protected OpenAiCompletionUsage usage;
    protected String system_fingerprint;
}
