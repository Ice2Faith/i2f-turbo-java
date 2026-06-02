package i2f.springboot.ops.openai.data.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:31
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiToolsDefinition {
    protected String type = OpenAiConsts.FUNCTION;
    protected Map<String, Object> function;

    public OpenAiToolsDefinition(Map<String, Object> function) {
        this.function = function;
    }

}
