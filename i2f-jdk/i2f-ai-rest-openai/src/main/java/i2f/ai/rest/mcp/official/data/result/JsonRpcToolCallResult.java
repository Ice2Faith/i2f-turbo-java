package i2f.ai.rest.mcp.official.data.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public static JsonRpcToolCallResult success(String text) {
        return of(text, false);
    }

    public static JsonRpcToolCallResult error(String msg) {
        return of(msg, true);
    }

    public static JsonRpcToolCallResult of(String text, boolean isError) {
        JsonRpcToolCallResult ret = new JsonRpcToolCallResult();
        ret.setContent(new ArrayList<>());
        ret.setError(isError);

        Map<String, Object> textContent = new LinkedHashMap<>();
        textContent.put("type", "text");
        textContent.put("text", text);

        ret.getContent().add(textContent);

        return ret;
    }
}
