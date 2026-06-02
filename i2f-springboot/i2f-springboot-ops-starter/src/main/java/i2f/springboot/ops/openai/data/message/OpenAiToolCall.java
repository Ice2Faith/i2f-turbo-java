package i2f.springboot.ops.openai.data.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:28
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiToolCall {
    protected String id;
    protected String type = OpenAiConsts.FUNCTION;
    protected OpenAiToolCallFunction function;

    public OpenAiToolCall(String id, OpenAiToolCallFunction function) {
        this.id = id;
        this.function = function;
    }
}
