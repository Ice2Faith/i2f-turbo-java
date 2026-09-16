package i2f.springboot.ai.mcp.client.stream.data;

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
    protected String jsonrpc;
    protected Long id;
    protected T result;
}
