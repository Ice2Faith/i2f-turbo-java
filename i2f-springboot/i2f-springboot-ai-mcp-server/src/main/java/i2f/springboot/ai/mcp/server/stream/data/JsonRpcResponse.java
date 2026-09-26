package i2f.springboot.ai.mcp.server.stream.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import i2f.springboot.ai.mcp.server.stream.consts.OfficialMcpConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 官方 MCP (Streamable HTTP) JSON-RPC 2.0 响应体
 * <p>
 * 成功时仅返回 result 字段，error 字段为空时不序列化，
 * 以保持与 {@code i2f-springboot-ai-mcp-client} 中 JsonRpcResponse 的解析兼容。
 *
 * @author Ice2Faith
 * @desc
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRpcResponse<T> {
    protected String jsonrpc = OfficialMcpConstants.JSON_RPC_VERSION;
    protected Long id;
    protected T result;
    protected JsonRpcError error;
}
