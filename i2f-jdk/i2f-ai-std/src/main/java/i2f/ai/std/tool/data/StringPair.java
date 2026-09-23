package i2f.ai.std.tool.data;

import i2f.ai.std.tool.annotations.ToolParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/9/15 19:28
 * @desc
 */
@Data
@NoArgsConstructor
public class StringPair {
    @ToolParam(description = "pair key, not null")
    protected String key;

    @ToolParam(description = "pair value, cloud be null")
    protected String value;
}
