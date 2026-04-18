package i2f.turbo.idea.plugin.inject.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:14
 * @desc
 */
public class JsonUtils {
    public static final ObjectMapper objectMapper = initObjectMapper();

    protected static ObjectMapper initObjectMapper() {
        ObjectMapper ret = new ObjectMapper();
        ret.configure(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true);
        ret.configure(JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature(), true);
        ret.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        ret.configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);
        ret.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
        ret.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        ret.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ret.setLocale(Locale.CHINA);
        ret.setTimeZone(TimeZone.getDefault());
        return ret;
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T parseJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T parseJson(String json, TypeReference<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T copy(Object obj, Class<T> type) {
        if (obj == null) {
            return null;
        }
        return parseJson(toJson(obj), type);
    }

    public static <T> T copy(Object obj, TypeReference<T> type) {
        if (obj == null) {
            return null;
        }
        return parseJson(toJson(obj), type);
    }

    public static <T> List<T> copyList(Collection<?> list, Class<T> type) {
        List<T> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (Object item : list) {
            ret.add(copy(item, type));
        }
        return ret;
    }

    public static <T> List<T> copyList(Collection<?> list, TypeReference<T> type) {
        List<T> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (Object item : list) {
            ret.add(copy(item, type));
        }
        return ret;
    }

}
