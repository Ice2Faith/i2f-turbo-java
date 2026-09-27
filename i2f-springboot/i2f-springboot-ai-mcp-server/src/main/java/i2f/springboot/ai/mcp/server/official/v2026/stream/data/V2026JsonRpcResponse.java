package i2f.springboot.ai.mcp.server.official.v2026.stream.data;

import i2f.springboot.ai.mcp.server.official.v2026.stream.consts.OfficialMcpV2026Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 官方 MCP 2026-07-28 无状态版本的 JSON-RPC 2.0 响应信封。
 * <p>
 * result 与 error 互斥，序列化时通过 {@link #toMap()} 仅输出其中一个，避免对端解析歧义。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP JSON-RPC 响应信封
 */
@Data
@NoArgsConstructor
public class V2026JsonRpcResponse {
    protected String jsonrpc = OfficialMcpV2026Constants.JSON_RPC_VERSION;
    protected String id;
    protected Object result;
    protected V2026JsonRpcError error;

    public static V2026JsonRpcResponse success(String id, Object result) {
        V2026JsonRpcResponse ret = new V2026JsonRpcResponse();
        ret.setId(id);
        ret.setResult(result);
        return ret;
    }

    public static V2026JsonRpcResponse error(String id, int code, String message) {
        return error(id, code, message, null);
    }

    public static V2026JsonRpcResponse error(String id, int code, String message, Map<String, Object> data) {
        V2026JsonRpcResponse ret = new V2026JsonRpcResponse();
        ret.setId(id);
        ret.setError(new V2026JsonRpcError(code, message, data));
        return ret;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("jsonrpc", jsonrpc);
        ret.put("id", id);
        if (result != null) {
            ret.put("result", result);
        }
        if (error != null) {
            ret.put("error", error.toMap());
        }
        return ret;
    }
}
