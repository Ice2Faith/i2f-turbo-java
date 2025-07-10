package i2f.extension.jackson.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonLong2StringSerializer extends JsonSerializer<Long> implements ContextualSerializer {
    public JacksonLong2StringSerializer() {
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return this;
        }
        if (beanProperty != null) {
            JsonFormat ann = beanProperty.getAnnotation(JsonFormat.class);
            if (ann != null) {
                return new NumberSerializer(Long.class);
            }
        }
        if (beanProperty != null) {
            Long2String ann = beanProperty.getAnnotation(Long2String.class);
            if (ann == null) {
                ann = beanProperty.getAnnotation(Long2String.class);
            }
            if (ann != null && ann.value()) {
                return ToStringSerializer.instance;
            }
        }
        return this;
    }

    @Override
    public void serialize(Long obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (obj != null) {
            jsonGenerator.writeNumber(obj);
        } else {
            serializerProvider.defaultSerializeNull(jsonGenerator);
        }
    }

}
