package i2f.net.http.data;

import java.util.*;
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
        mergeNames(key);
        return this;
    }

    public HttpHeaders mergeNames(String name) {
        List<String> realNames = getRealNames(name);
        ArrayList<String> list = new ArrayList<>();
        for (String item : realNames) {
            ArrayList<String> arr = get(item);
            if (arr != null) {
                list.addAll(arr);
            }
        }
        for (String item : realNames) {
            remove(item);
        }
        put(name, list);
        return this;
    }

    public HttpHeaders set(String key, Object value) {
        remove(key);
        add(key, value);
        return this;
    }

    public HttpHeaders setAll(Map<String, ?> map) {
        if (map != null) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                set(entry.getKey(), value);
            }
        }
        return this;
    }

    public List<String> getHeader(String name) {
        List<String> ret = new ArrayList<>();
        List<String> realNames = getRealNames(name);
        for (String item : realNames) {
            ArrayList<String> list = get(item);
            if (list != null) {
                ret.addAll(list);
            }
        }
        return ret;
    }

    public String getFirstHeader(String name) {
        List<String> realNames = getRealNames(name);
        for (String item : realNames) {
            ArrayList<String> list = get(item);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    public List<String> getRealNames(String name) {
        List<String> ret = new ArrayList<>();
        Set<String> keys = keySet();
        for (String item : keys) {
            if (name == item
                    || (name != null && name.equalsIgnoreCase(item))
                    || (item != null && item.equalsIgnoreCase(name))
            ) {
                ret.add(item);
            }
        }
        return ret;
    }

    public String valueToString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
