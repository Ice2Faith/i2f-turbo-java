package i2f.ai.rest.mcp.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/13 15:12
 * @desc
 */
@Data
@NoArgsConstructor
public class McpCallPayloadDto {
    protected String content;
    protected String context;
}
