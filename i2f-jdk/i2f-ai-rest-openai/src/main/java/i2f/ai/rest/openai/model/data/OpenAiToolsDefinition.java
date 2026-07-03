package i2f.ai.rest.openai.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:31
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolsDefinition implements BaseMutator<OpenAiToolsDefinition> {
    protected String type = OpenAiConsts.FUNCTION;
    protected Map<String, Object> function;

    public OpenAiToolsDefinition(Map<String, Object> function) {
        this.function = function;
    }

}
