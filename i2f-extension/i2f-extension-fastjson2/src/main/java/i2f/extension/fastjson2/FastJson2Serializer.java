package i2f.extension.fastjson2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import i2f.serialize.str.json.IJsonSerializer;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 21:29
 * @desc
 */
public class FastJson2Serializer implements IJsonSerializer {
    public static FastJson2Serializer INSTANCE = new FastJson2Serializer();

    @Override
    public String serialize(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Object deserialize(String enc) {
        return JSON.parse(enc);
    }

    @Override
    public Object deserialize(String text, Class<?> clazz) {
        return JSON.parseObject(text, clazz);
    }

    @Override
    public Object deserialize(String text, Object typeToken) {
        return deserialize(text, (TypeReference) typeToken);
    }

    public <T> T deserialize(String text, TypeReference<T> typeToken) {
        return JSON.parseObject(text, typeToken);
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return (Map<String, Object>) deserialize(json, new TypeReference<Map<String, Object>>() {
        });
    }
}
