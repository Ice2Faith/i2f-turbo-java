package i2f.ai.rest.mcp.official.v2024.data;

import i2f.ai.rest.mcp.official.v2024.consts.OfficialMcpConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 17:54
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcResponse<T> {
    protected String jsonrpc = OfficialMcpConstants.JSON_RPC_VERSION;
    protected String id;
    protected T result;
    protected JsonRpcError error;

    public static <T> JsonRpcResponse<T> success(String id, T result) {
        JsonRpcResponse<T> ret = new JsonRpcResponse<>();
        ret.setJsonrpc(OfficialMcpConstants.JSON_RPC_VERSION);
        ret.setId(id);
        ret.setResult(result);
        return ret;
    }

    public static JsonRpcResponse<?> error(String id, int code, String message) {
        JsonRpcResponse<?> ret = new JsonRpcResponse<>();
        ret.setJsonrpc(OfficialMcpConstants.JSON_RPC_VERSION);
        ret.setId(id);
        ret.setError(new JsonRpcError(code, message));
        return ret;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("jsonrpc", jsonrpc);
        ret.put("id", id);
        if (result != null) {
            ret.put("result", result);
        }
        if (error != null) {
            ret.put("error", error);
        }
        return ret;
    }
}
