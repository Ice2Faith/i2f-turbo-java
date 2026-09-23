package i2f.extension.fory.json;

import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import org.apache.fory.json.ForyJson;
import org.apache.fory.reflect.TypeRef;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/21 9:52
 * @desc
 */
@Data
public class ForyJsonSerializer implements IJsonSerializer {
    public static ForyJsonSerializer INSTANCE = new ForyJsonSerializer();

    public static final ForyJson JSON = ForyJson.builder().build();

    protected final ForyJson fory;

    public ForyJsonSerializer() {
        this.fory = JSON;
    }

    public ForyJsonSerializer(ForyJson fory) {
        this.fory = fory;
    }

    @Override
    public String serialize(Object obj) {
        return fory.toJson(obj);
    }

    @Override
    public Object deserialize(String enc) {
        return fory.fromJson(enc, Object.class);
    }

    @Override
    public Object deserialize(String text, Class<?> clazz) {
        return fory.fromJson(text, clazz);
    }

    @Override
    public Object deserialize(String text, Object typeToken) {
        if (typeToken instanceof Type) {
            return deserialize(text, (Type) typeToken);
        } else if (typeToken instanceof TypeRef) {
            return deserialize(text, (TypeRef) typeToken);
        }
        throw new UnsupportedOperationException("ForyJson un-support parseText.");
    }

    public <T> T deserialize(String text, TypeRef<T> typeToken) {
        return fory.fromJson(text, typeToken);
    }

    public <T> T deserialize(String text, Type typeToken) {
        return fory.fromJson(text, TypeRef.of(typeToken));
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return (Map<String, Object>) deserialize(json, new TypeRef<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> deserializeAsMap(String text) {
        return deserialize(text, new TypeRef<Map<String, Object>>() {
        });
    }
}
