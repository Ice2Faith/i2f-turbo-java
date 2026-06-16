package i2f.mixin.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:01
 * @desc
 */
public interface MapMixins {
    default <K, V> Map<K, V> new_map() {
        return new HashMap<>();
    }

    default void put(Map map, Object key, Object value) {
        map.put(key, value);
    }

    default Object map_get(Map map, Object key) {
        return map.get(key);
    }

    default boolean map_contains(Map map, Object key) {
        return map.containsKey(key);
    }

    default Object map_remove(Map map, Object key) {
        return map.remove(key);
    }

    default void clear(Map map) {
        if (map == null) {
            return;
        }
        map.clear();
    }

    default int length(Map map) {
        if (map == null) {
            return -1;
        }
        return map.size();
    }

    default Map<Object, Object> map_of(Object... arr) {
        Map<Object, Object> ret = new LinkedHashMap<>();
        for (int i = 0; i < arr.length; i += 2) {
            if (i + 1 < arr.length) {
                ret.put(arr[i], arr[i + 1]);
            } else {
                ret.put(arr[i], null);
            }
        }
        return ret;
    }
}
