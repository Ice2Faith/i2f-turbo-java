package i2f.spring.ai.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 14:27
 * @desc
 */
public class SpringAiJsonSerializer implements IJsonSerializer {
    public static final SpringAiJsonSerializer INSTANCE = new SpringAiJsonSerializer();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> deserializeAsMap(String enc) {
        try {
            return objectMapper.readValue(enc, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(String enc) {
        try {
            return objectMapper.readValue(enc, Object.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        try {
            return objectMapper.readValue(enc, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(String enc, Object type) {
        try {
            return objectMapper.readValue(enc, (TypeReference) type);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
