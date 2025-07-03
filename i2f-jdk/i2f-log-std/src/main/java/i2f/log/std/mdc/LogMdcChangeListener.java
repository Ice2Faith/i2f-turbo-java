package i2f.log.std.mdc;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/7/3 16:38
 */
@FunctionalInterface
public interface LogMdcChangeListener {
    void change(LogMdcChangeType type, String key, Object value, Object oldValue, Map<String, Object> mdcMap);
}
