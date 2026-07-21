package i2f.springboot.ai.mcp.client.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 18:17
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolCallResult {
    protected List<Map<String, Object>> content;
    private boolean isError;
}
