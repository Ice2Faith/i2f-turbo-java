package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/6/2 14:28
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiToolDefinitionDto {
    protected String name;
    protected String description;
    protected List<String> parameterNames;
    protected Set<String> tags;
    protected String bindMethod;
    protected String bindClass;
}
