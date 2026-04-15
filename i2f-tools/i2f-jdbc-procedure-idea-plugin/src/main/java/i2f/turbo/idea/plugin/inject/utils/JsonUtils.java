package i2f.turbo.idea.plugin.inject.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:14
 * @desc
 */
public class JsonUtils {
    public static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T parseJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static <T> T parseJson(String json, TypeToken<T> type) {
        return gson.fromJson(json, type.getType());
    }

}
