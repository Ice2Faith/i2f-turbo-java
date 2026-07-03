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
public class OpenAiToolCallFunction implements BaseMutator<OpenAiToolCallFunction> {
    protected String name;
    protected String arguments;

    public OpenAiToolCallFunction(String name, String arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
