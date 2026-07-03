package i2f.ai.rest.openai.model.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/24 21:05
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiCompletionUsage implements BaseBuilder<OpenAiCompletionUsage> {
    protected Long prompt_tokens;
    protected Long completion_tokens;
    protected Long total_tokens;
}
