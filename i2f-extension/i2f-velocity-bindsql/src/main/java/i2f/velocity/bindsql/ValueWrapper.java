package i2f.velocity.bindsql;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/31 23:23
 * @desc
 */
public class ValueWrapper {
    private Map<String, Object> map = new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    public Object wrap(Object value) {
        String key = "${" + map.size() + "}";
        map.put(key, value);
        return key;
    }
}
