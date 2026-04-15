package i2f.extension.slf4j;

import i2f.trace.mdc.manager.MdcManager;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/15 14:41
 * @desc
 */
public class Slf4jMdcManager implements MdcManager {

    @Override
    public void put(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    public String get(String key) {
        return MDC.get(key);
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }

    @Override
    public void clear() {
        MDC.clear();
    }

    @Override
    public Map<String, String> copyOf() {
        return MDC.getCopyOfContextMap();
    }

    @Override
    public void replaceAs(Map<String, String> map) {
        MDC.setContextMap(map);
    }
}
