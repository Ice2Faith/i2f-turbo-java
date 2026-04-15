package i2f.extension.ai.langchain4j8.model;

import com.google.gson.Gson;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.typeof.token.TypeToken;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 14:05
 * @desc
 */
@NoArgsConstructor
public class Langchain4j8JsonSerializer implements IJsonSerializer {
    public static final Langchain4j8JsonSerializer INSTANCE = new Langchain4j8JsonSerializer();

    protected Gson gson = new Gson();

    public Langchain4j8JsonSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Map<String, Object> deserializeAsMap(String enc) {
        return gson.fromJson(enc, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    @Override
    public String serialize(Object data) {
        return gson.toJson(data);
    }

    @Override
    public Object deserialize(String enc) {
        return gson.fromJson(enc, Object.class);
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        return gson.fromJson(enc, clazz);
    }

    @Override
    public Object deserialize(String enc, Object type) {
        return gson.fromJson(enc, (Type) type);
    }

}
