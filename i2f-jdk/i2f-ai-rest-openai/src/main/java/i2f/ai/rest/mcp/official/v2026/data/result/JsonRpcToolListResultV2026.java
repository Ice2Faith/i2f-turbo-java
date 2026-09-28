package i2f.ai.rest.mcp.official.v2026.data.result;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import i2f.ai.rest.mcp.official.v2024.data.result.JsonRpcToolListItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/28 14:26
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolListResultV2026 implements IJsonRpcDto {
    protected List<JsonRpcToolListItem> tools;
    protected String resultType;
    protected long ttlMs;
    protected String cacheScope;
    protected Map<String, Object> _meta;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("tools", tools);
        ret.put("resultType", resultType);
        ret.put("ttlMs", ttlMs);
        ret.put("cacheScope", cacheScope);
        ret.put("_meta", _meta);

        return ret;
    }
}
