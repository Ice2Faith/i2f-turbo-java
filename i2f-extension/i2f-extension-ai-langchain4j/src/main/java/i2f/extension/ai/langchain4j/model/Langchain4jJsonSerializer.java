package i2f.extension.ai.langchain4j.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/27 14:05
 * @desc
 */
public class Langchain4jJsonSerializer implements IJsonSerializer {
    public static final Langchain4jJsonSerializer INSTANCE = new Langchain4jJsonSerializer();

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
