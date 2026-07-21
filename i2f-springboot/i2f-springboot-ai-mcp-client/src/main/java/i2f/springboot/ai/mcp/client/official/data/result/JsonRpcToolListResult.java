package i2f.springboot.ai.mcp.client.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/7/21 18:06
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolListResult {
    protected List<JsonRpcToolListItem> tools;
}
