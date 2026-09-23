package i2f.ai.rest.openai.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:28
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolCall implements BaseMutator<OpenAiToolCall> {
    protected Integer index;
    protected String id;
    protected String type = OpenAiConsts.FUNCTION;
    protected OpenAiToolCallFunction function;

    public OpenAiToolCall(String id, OpenAiToolCallFunction function) {
        this.id = id;
        this.function = function;
    }
}
