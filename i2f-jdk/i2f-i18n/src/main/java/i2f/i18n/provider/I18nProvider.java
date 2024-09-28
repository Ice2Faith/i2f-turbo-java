package i2f.i18n.provider;

import i2f.i18n.I18n;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/9/28 14:34
 */
public interface I18nProvider {
    String get(String lang, String name);

    default String get(String name) {
        return get(I18n.getThreadLang(), name);
    }

    default String getOrDefault(String lang, String name, String defaultValue) {
        String value = get(lang, name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    default String getOrDefault(String name, String defaultValue) {
        return getOrDefault(I18n.getThreadLang(), name, defaultValue);
    }

    default String format(String lang, String name, Object... args) {
        return String.format(getOrDefault(lang, name, ""), args);
    }

    default String format(String name, Object... args) {
        return format(I18n.getThreadLang(), name, args);
    }

    default <T> T getAs(String lang, String name, Function<String, T> mapper, T defaultValue) {
        String value = get(lang, name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return mapper.apply(value);
        } catch (Exception e) {

        }
        return defaultValue;
    }

    default <T> T getAs(String name, Function<String, T> mapper, T defaultValue) {
        return getAs(I18n.getThreadLang(), name, mapper, defaultValue);
    }

    default int getInt(String lang, String name, int defaultValue) {
        return getAs(lang, name, Integer::parseInt, defaultValue);
    }

    default int getInt(String name, int defaultValue) {
        return getAs(I18n.getThreadLang(), name, Integer::parseInt, defaultValue);
    }

    default long getLong(String lang, String name, long defaultValue) {
        return getAs(lang, name, Long::parseLong, defaultValue);
    }

    default long getLong(String name, long defaultValue) {
        return getAs(I18n.getThreadLang(), name, Long::parseLong, defaultValue);
    }

    default double getDouble(String lang, String name, double defaultValue) {
        return getAs(lang, name, Double::parseDouble, defaultValue);
    }

    default double getDouble(String name, double defaultValue) {
        return getAs(I18n.getThreadLang(), name, Double::parseDouble, defaultValue);
    }

    default float getFloat(String lang, String name, float defaultValue) {
        return getAs(lang, name, Float::parseFloat, defaultValue);
    }

    default float getFloat(String name, float defaultValue) {
        return getAs(I18n.getThreadLang(), name, Float::parseFloat, defaultValue);
    }

    default boolean getBoolean(String lang, String name, boolean defaultValue) {
        return getAs(lang, name, Boolean::parseBoolean, defaultValue);
    }

    default boolean getBoolean(String name, boolean defaultValue) {
        return getAs(I18n.getThreadLang(), name, Boolean::parseBoolean, defaultValue);
    }
}
