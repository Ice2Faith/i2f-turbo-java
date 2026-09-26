package i2f.ai.rest.mcp.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/26 20:40
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolCallParam {
    protected String name;
    protected Map<String, Object> arguments;
}
