package i2f.ai.std.tool.data;

import i2f.ai.std.tool.annotations.ToolParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/15 19:25
 * @desc
 */
@Data
@NoArgsConstructor
public class StringPairMap {
    @ToolParam(description = "dict pair list")
    protected List<StringPair> pairList;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (StringPair pair : pairList) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

}
