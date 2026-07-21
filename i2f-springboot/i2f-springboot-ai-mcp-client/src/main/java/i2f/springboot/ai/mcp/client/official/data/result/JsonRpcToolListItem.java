package i2f.springboot.ai.mcp.client.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/21 18:07
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolListItem {
    protected String name;
    protected String description;
    protected Map<String, Object> inputSchema;
}
