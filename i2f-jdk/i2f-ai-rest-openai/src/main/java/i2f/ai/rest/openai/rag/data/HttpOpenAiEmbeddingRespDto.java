package i2f.ai.rest.openai.rag.data;

import i2f.builder.BaseBuilder;
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
public class HttpOpenAiEmbeddingRespDto implements BaseBuilder<HttpOpenAiEmbeddingRespDto> {
    protected String model;
    protected String object;
    protected List<EmbeddingResult> data;
    protected Usage usage;

    @Data
    @NoArgsConstructor
    public static class EmbeddingResult implements BaseBuilder<EmbeddingResult> {
        protected List<Double> embedding;
        protected Integer index;
        protected String object;
    }

    @Data
    @NoArgsConstructor
    public static class Usage implements BaseBuilder<Usage> {
        protected Long prompt_tokens;
        protected Long total_tokens;
    }
}
