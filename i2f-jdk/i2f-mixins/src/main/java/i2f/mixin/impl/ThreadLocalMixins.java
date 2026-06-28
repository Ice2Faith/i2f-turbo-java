package i2f.mixin.impl;


import i2f.mixin.consts.MixinConsts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:02
 * @desc
 */
public interface ThreadLocalMixins {

    default Map<String, Object> local_map() {
        Map<String, Object> map = MixinConsts.LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
            MixinConsts.LOCAL.set(map);
        }
        return map;
    }

    default Object local_get(String key) {
        return local_map().get(key);
    }

    default void local_set(String key, Object value) {
        local_map().put(key, value);
    }

    default void local_remove(String key) {
        local_map().remove(key);
    }

    default boolean local_contains(String key) {
        return local_map().containsKey(key);
    }

    default void local_reset() {
        MixinConsts.LOCAL.set(new HashMap<>());
    }

    default void local_clear() {
        local_map().clear();
    }
}
