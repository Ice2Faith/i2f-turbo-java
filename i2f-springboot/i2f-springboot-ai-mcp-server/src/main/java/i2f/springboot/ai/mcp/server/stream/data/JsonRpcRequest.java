package i2f.springboot.ai.mcp.server.stream.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 官方 MCP (Streamable HTTP) JSON-RPC 2.0 请求体
 *
 * @author Ice2Faith
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcRequest {
    protected String jsonrpc;
    protected Long id;
    protected String method;
    protected Map<String, Object> params;
}
