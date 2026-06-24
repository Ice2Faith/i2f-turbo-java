package i2f.net.http.rest.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:53
 * @desc
 */
public class HttpHeaders extends LinkedHashMap<String, ArrayList<String>> {
    public HttpHeaders(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public HttpHeaders(int initialCapacity) {
        super(initialCapacity);
    }

    public HttpHeaders() {
    }

    public HttpHeaders(Map<? extends String, ? extends ArrayList<String>> m) {
        super(m);
    }

    public HttpHeaders(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static HttpHeaders create() {
        return new HttpHeaders();
    }

    public HttpHeaders addAll(Map<String, ?> map) {
        if (map != null) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                add(entry.getKey(), value);
            }
        }
        return this;
    }

    public HttpHeaders apply(Consumer<HttpHeaders> consumer) {
        consumer.accept(this);
        return this;
    }

    public HttpHeaders add(String key, Object value) {
        ArrayList<String> list = computeIfAbsent(key, k -> new ArrayList<>());
        if (value instanceof Collection) {
            Collection<?> col = (Collection<?>) value;
            for (Object item : col) {
                list.add(valueToString(item));
            }
        }
        list.add(valueToString(value));
        return this;
    }

    public String valueToString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
