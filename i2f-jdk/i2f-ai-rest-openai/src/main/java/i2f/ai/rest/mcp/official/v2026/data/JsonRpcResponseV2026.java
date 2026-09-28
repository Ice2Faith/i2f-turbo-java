package i2f.ai.rest.mcp.official.v2026.data;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
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
public class JsonRpcResponseV2026<T> implements IJsonRpcDto {
    protected String jsonrpc = OfficialMcpConstantsV2026.JSON_RPC_VERSION;
    protected String id;
    protected T result;
    protected JsonRpcErrorV2026 error;

    public static <T> JsonRpcResponseV2026<T> success(String id, T result) {
        JsonRpcResponseV2026<T> ret = new JsonRpcResponseV2026<>();
        ret.setId(id);
        ret.setResult(result);
        return ret;
    }

    public static <T> JsonRpcResponseV2026<T> error(String id, int code, String message) {
        return error(id, code, message, null);
    }

    public static <T> JsonRpcResponseV2026<T> error(String id, int code, String message, Map<String, Object> data) {
        JsonRpcResponseV2026<T> ret = new JsonRpcResponseV2026<>();
        ret.setId(id);
        ret.setError(new JsonRpcErrorV2026(code, message, data));
        return ret;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("jsonrpc", jsonrpc);
        ret.put("id", id);
        if (result != null) {
            Object obj = result;
            if (obj instanceof IJsonRpcDto) {
                IJsonRpcDto dto = (IJsonRpcDto) obj;
                obj = dto.toMap();
            }
            ret.put("result", obj);
        }
        if (error != null) {
            ret.put("error", error.toMap());
        }
        return ret;
    }
}
