package i2f.ai.rest.openai.model.data;

import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:31
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolsDefinition implements BaseMutator<OpenAiToolsDefinition> {
    protected String type = OpenAiConsts.FUNCTION;
    protected FunctionJsonSchema function;

    public OpenAiToolsDefinition(FunctionJsonSchema function) {
        this.function = function;
    }

}
