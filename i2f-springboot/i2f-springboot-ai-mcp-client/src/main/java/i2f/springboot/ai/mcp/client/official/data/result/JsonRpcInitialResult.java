package i2f.springboot.ai.mcp.client.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 18:25
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcInitialResult {
    protected String protocolVersion;
    protected Map<String, Object> capabilities;
    protected Map<String, Object> serverInfo;
}
