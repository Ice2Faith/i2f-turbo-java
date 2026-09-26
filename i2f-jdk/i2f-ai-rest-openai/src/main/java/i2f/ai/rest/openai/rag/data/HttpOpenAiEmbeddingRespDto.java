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
public class HttpOpenAiEmbeddingRespDto implements BaseMutator<HttpOpenAiEmbeddingRespDto> {
    protected String model;
    protected String object;
    protected List<EmbeddingResult> data;
    protected Usage usage;

    @Data
    @NoArgsConstructor
    public static class EmbeddingResult implements BaseMutator<EmbeddingResult> {
        protected List<Double> embedding;
        protected Integer index;
        protected String object;
    }

    @Data
    @NoArgsConstructor
    public static class Usage implements BaseMutator<Usage> {
        protected Long prompt_tokens;
        protected Long total_tokens;
    }
}
