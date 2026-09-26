package i2f.springboot.ai.mcp.server.stream.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 官方 MCP (Streamable HTTP) JSON-RPC 2.0 错误对象
 *
 * @author Ice2Faith
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcError {
    protected int code;
    protected String message;

    public JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
