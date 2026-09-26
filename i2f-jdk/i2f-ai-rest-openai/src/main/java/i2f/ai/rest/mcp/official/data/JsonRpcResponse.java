package i2f.ai.rest.mcp.official.data;

import i2f.ai.rest.mcp.official.consts.OfficialMcpConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/21 17:54
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcResponse<T> {
    protected String jsonrpc = OfficialMcpConstants.JSON_RPC_VERSION;
    protected Long id;
    protected T result;
    protected JsonRpcError error;

    public static <T> JsonRpcResponse<T> success(Long id, T result) {
        JsonRpcResponse<T> ret = new JsonRpcResponse<>();
        ret.setJsonrpc(OfficialMcpConstants.JSON_RPC_VERSION);
        ret.setId(id);
        ret.setResult(result);
        return ret;
    }

    public static JsonRpcResponse<?> error(Long id, int code, String message) {
        JsonRpcResponse<?> ret = new JsonRpcResponse<>();
        ret.setJsonrpc(OfficialMcpConstants.JSON_RPC_VERSION);
        ret.setId(id);
        ret.setError(new JsonRpcError(code, message));
        return ret;
    }
}
