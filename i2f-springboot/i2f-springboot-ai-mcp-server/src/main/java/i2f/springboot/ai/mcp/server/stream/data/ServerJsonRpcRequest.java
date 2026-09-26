package i2f.springboot.ai.mcp.server.stream.data;

import i2f.ai.rest.mcp.official.data.JsonRpcRequest;
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
public class ServerJsonRpcRequest extends JsonRpcRequest<Map<String, Object>> {

}
