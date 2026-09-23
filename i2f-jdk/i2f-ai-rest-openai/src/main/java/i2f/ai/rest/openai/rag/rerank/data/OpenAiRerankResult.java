package i2f.ai.rest.openai.rag.rerank.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/25 9:05
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiRerankResult implements BaseMutator<OpenAiRerankResult> {
    protected Integer index;
    protected Double relevance_score;
    protected OpenAiRerankResultDocument document;
}
