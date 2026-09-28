package i2f.ai.rest.mcp.official.v2026.data.result;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/28 14:13
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcServerDiscoverResult implements IJsonRpcDto {
    protected String resultType;
    protected List<String> supportedVersions;
    protected Map<String, Object> capabilities;
    protected Map<String, Object> _meta;
    protected String instructions;
    protected long ttlMs;
    protected String cacheScope;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("resultType", resultType);
        ret.put("supportedVersions", supportedVersions);
        ret.put("capabilities", capabilities);
        ret.put("_meta", _meta);
        if (instructions != null && !instructions.isEmpty()) {
            ret.put("instructions", instructions);
        }
        ret.put("ttlMs", ttlMs);
        ret.put("cacheScope", cacheScope);
        return ret;
    }
}
