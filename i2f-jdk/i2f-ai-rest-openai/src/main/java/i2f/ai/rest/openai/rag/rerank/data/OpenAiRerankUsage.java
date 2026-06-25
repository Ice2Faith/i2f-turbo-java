package i2f.ai.rest.openai.rag.rerank.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/25 9:07
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiRerankUsage {
    protected Long prompt_tokens;
    protected Long total_tokens;
}
