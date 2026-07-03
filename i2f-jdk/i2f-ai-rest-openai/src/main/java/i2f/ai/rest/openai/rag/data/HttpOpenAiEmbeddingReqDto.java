package i2f.ai.rest.openai.rag.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/24 17:00
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiEmbeddingReqDto implements BaseMutator<HttpOpenAiEmbeddingReqDto> {
    protected String model;
    protected List<String> input;
}
