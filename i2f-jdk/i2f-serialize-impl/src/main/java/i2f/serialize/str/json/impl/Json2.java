package i2f.serialize.str.json.impl;

import i2f.reflect.RichConverter;
import i2f.typeof.token.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@Data
@NoArgsConstructor
public class Json2 {
    public static final JsonGenerator INSTANCE = JsonGenerator.INSTANCE;
    public static final JsonGenerator INSTANCE_WITHOUT_NULL = JsonGenerator.INSTANCE_WITHOUT_NULL;

    public static String toJson(Object obj) {
        return INSTANCE.toJson(obj);
    }

    public static String toJson(Object obj, boolean withoutNull) {
        if (withoutNull) {
            return INSTANCE_WITHOUT_NULL.toJson(obj);
        }
        return INSTANCE.toJson(obj);
    }

    public static Object parseJson(String json) {
        return JsonParser.parse(json);
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        return parseJson(json, clazz, true);
    }

    public static <T> T parseJson(String json, Class<T> clazz, boolean weakMatchField) {
        Object obj = parseJson(json);
        return RichConverter.convert(obj, clazz, weakMatchField);
    }

    public static <T> T parseJson(String json, Type type) {
        return parseJson(json, type, true);
    }

    public static <T> T parseJson(String json, Type type, boolean weakMatchField) {
        Object obj = parseJson(json);
        return RichConverter.convert2Type(obj, type, weakMatchField);
    }

    public static <T> T parseJson(String json, TypeToken<T> type) {
        return parseJson(json, type, true);
    }

    public static <T> T parseJson(String json, TypeToken<T> type, boolean weakMatchField) {
        Object obj = parseJson(json);
        return RichConverter.convert(obj, type, weakMatchField);
    }

}
