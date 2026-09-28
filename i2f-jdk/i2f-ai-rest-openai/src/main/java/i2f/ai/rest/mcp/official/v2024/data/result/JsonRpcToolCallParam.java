package i2f.ai.rest.mcp.official.v2024.data.result;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/26 20:40
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolCallParam implements IJsonRpcDto {
    protected String name;
    protected Map<String, Object> arguments;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", name);
        ret.put("arguments", arguments);

        return ret;
    }
}
