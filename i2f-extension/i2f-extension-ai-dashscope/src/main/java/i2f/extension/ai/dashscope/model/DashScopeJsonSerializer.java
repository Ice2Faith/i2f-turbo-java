package i2f.extension.ai.dashscope.model;

import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 14:02
 * @desc
 */
public class DashScopeJsonSerializer implements IJsonSerializer {
    public static final DashScopeJsonSerializer INSTANCE = new DashScopeJsonSerializer();

    @Override
    public Map<String, Object> deserializeAsMap(String enc) {
        return JsonUtils.fromJson(enc, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    @Override
    public String serialize(Object data) {
        return JsonUtils.toJson(data);
    }

    @Override
    public Object deserialize(String enc) {
        return JsonUtils.fromJson(enc, Object.class);
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        return JsonUtils.fromJson(enc, clazz);
    }

    @Override
    public Object deserialize(String enc, Object type) {
        return JsonUtils.fromJson(enc, (Type) type);
    }

}
