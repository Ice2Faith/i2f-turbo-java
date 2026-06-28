package i2f.extension.jackson.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.JsonMappingException;
import tools.jackson.databind.JsonSerializer;
import tools.jackson.databind.SerializerProvider;
import tools.jackson.databind.ser.ContextualSerializer;
import tools.jackson.databind.ser.std.NumberSerializer;
import tools.jackson.databind.ser.std.ToStringSerializer;

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
