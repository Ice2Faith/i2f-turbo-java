package i2f.ai.rest.openai.rag.rerank.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/25 9:06
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiRerankResultDocument implements BaseMutator<OpenAiRerankResultDocument> {
    protected String text;
}
