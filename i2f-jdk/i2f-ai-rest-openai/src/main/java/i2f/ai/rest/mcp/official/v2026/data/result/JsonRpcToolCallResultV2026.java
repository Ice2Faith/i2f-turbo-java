package i2f.ai.rest.mcp.official.v2026.data.result;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/9/28 14:30
 * @desc
 */
@Data
@NoArgsConstructor
public class JsonRpcToolCallResultV2026 implements IJsonRpcDto {
    protected List<Map<String, Object>> content;
    protected boolean isError;
    protected String resultType;
    protected Map<String, Object> _meta;

    public static JsonRpcToolCallResultV2026 success(String text) {
        return of(text, false);
    }

    public static JsonRpcToolCallResultV2026 error(String msg) {
        return of(msg, true);
    }

    public static JsonRpcToolCallResultV2026 of(String text, boolean isError) {
        JsonRpcToolCallResultV2026 result = new JsonRpcToolCallResultV2026();
        result.setError(isError);
        result.setContent(new ArrayList<>());

        Map<String, Object> textContent = new LinkedHashMap<>();
        textContent.put("type", "text");
        textContent.put("text", text);
        result.getContent().add(textContent);

        result.setResultType(OfficialMcpConstantsV2026.RESULT_TYPE_COMPLETE);
        result.set_meta(new HashMap<>());
        return result;
    }

    public JsonRpcToolCallResultV2026 withMeta(Map<String, Object> meta) {
        this._meta = meta;
        return this;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("content", content);
        ret.put("isError", isError);
        ret.put("resultType", resultType);
        ret.put("_meta", _meta);

        return ret;
    }
}
