package i2f.ai.rest.mcp.official.v2024.data;

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
    protected String id;
    protected String method;
    protected T params;

}
