package i2f.springboot.ai.mcp.server.official.v2026.stream.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 官方 MCP 2026-07-28 无状态版本的 JSON-RPC 2.0 错误对象。
 * <p>
 * data 字段用于承载协议级错误附加信息，例如 UnsupportedProtocolVersionError 需要回带
 * {@code supported}（服务端支持的版本列表）与 {@code requested}（请求声明的版本）。
 *
 * @author Ice2Faith
 * @desc 2026-07-28 无状态 MCP JSON-RPC 错误对象
 */
@Data
@NoArgsConstructor
public class V2026JsonRpcError {
    protected int code;
    protected String message;
    protected Map<String, Object> data;

    public V2026JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public V2026JsonRpcError(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("code", code);
        ret.put("message", message);
        if (data != null && !data.isEmpty()) {
            ret.put("data", data);
        }
        return ret;
    }
}
