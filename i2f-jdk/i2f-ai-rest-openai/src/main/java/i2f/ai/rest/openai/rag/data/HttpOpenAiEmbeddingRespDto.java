package i2f.ai.rest.openai.rag.data;

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
public class HttpOpenAiEmbeddingRespDto {
    protected String model;
    protected String object;
    protected List<EmbeddingResult> data;
    protected Usage usage;

    @Data
    @NoArgsConstructor
    public static class EmbeddingResult {
        protected List<Double> embedding;
        protected Integer index;
        protected String object;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        protected Long prompt_tokens;
        protected Long total_tokens;
    }
}
