package i2f.resources.provider;


import i2f.io.stream.StreamUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2025/1/14 11:09
 */
public interface ResourceProvider {
    InputStream get(String id);

    default InputStream getBundle(String id, String... suffixes) {
        for (String suffix : suffixes) {
            if (suffix == null) {
                suffix = "";
            }
            InputStream is = get(id + suffix);
            if (is != null) {
                return is;
            }
        }
        return null;
    }

    default String getString(String id, String charset) {
        try (InputStream is = get(id)) {
            return StreamUtil.readString(is, charset, true);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    default byte[] getBytes(String id) {
        try (InputStream is = get(id)) {
            return StreamUtil.readBytes(is, true);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    default String getString(String id) {
        return getString(id, StandardCharsets.UTF_8.name());
    }

    @FunctionalInterface
    interface ExFunction<T, R> {
        R apply(T val) throws Throwable;
    }

    default <T> T getAs(String id, ExFunction<String, T> parser, T def) {
        try {
            String val = getString(id);
            return parser.apply(val);
        } catch (Throwable e) {

        }
        return def;
    }

    default Integer getInteger(String id, Integer def) {
        return getAs(id, Integer::parseInt, def);
    }

    default Long getLong(String id, Long def) {
        return getAs(id, Long::parseLong, def);
    }

    default Boolean getBoolean(String id, Boolean def) {
        return getAs(id, Boolean::parseBoolean, def);
    }

    default Float getFloat(String id, Float def) {
        return getAs(id, Float::parseFloat, def);
    }

    default Double getDouble(String id, Double def) {
        return getAs(id, Double::parseDouble, def);
    }
}
