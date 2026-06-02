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
public class OpenAiToolCallFunction {
    protected String name;
    protected String arguments;

    public OpenAiToolCallFunction(String name, String arguments) {
        this.name = name;
        this.arguments = arguments;
    }
}
