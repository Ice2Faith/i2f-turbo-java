package i2f.environment;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/9 8:44
 * @desc
 */
public interface IEnvironment {
    String getProperty(String name);

    Map<String, String> getAllProperties();

    default String getProperty(String name, String defVal) {
        String prop = getProperty(name);
        return prop == null ? defVal : prop;
    }

    default Integer getInteger(String name) {
        return getInteger(name, null);
    }

    default Integer getInteger(String name, Integer defVal) {
        String prop = getProperty(name);
        try {
            return Integer.parseInt(prop);
        } catch (Exception e) {

        }
        return defVal;
    }

    default Long getLong(String name) {
        return getLong(name, null);
    }

    default Long getLong(String name, Long defVal) {
        String prop = getProperty(name);
        try {
            return Long.parseLong(prop);
        } catch (Exception e) {

        }
        return defVal;
    }

    default Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }

    default Boolean getBoolean(String name, Boolean defVal) {
        String prop = getProperty(name);
        try {
            return Boolean.parseBoolean(prop);
        } catch (Exception e) {

        }
        return defVal;
    }

    default Double getDouble(String name) {
        return getDouble(name, null);
    }

    default Double getDouble(String name, Double defVal) {
        String prop = getProperty(name);
        try {
            return Double.parseDouble(prop);
        } catch (Exception e) {

        }
        return defVal;
    }
}
