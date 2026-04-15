package i2f.trace.mdc.manager;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/15 14:14
 * @desc
 */
public interface MdcManager {
    void put(String key, String value);

    String get(String key);

    void remove(String key);

    void clear();

    Map<String, String> copyOf();

    void replaceAs(Map<String, String> map);
}
