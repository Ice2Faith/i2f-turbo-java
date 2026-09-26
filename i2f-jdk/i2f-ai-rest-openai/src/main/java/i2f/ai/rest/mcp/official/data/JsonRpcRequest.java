package i2f.ai.rest.mcp.official.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/21 17:52
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcRequest<T> {
    protected String jsonrpc;
    protected Long id;
    protected String method;
    protected T params;

}
