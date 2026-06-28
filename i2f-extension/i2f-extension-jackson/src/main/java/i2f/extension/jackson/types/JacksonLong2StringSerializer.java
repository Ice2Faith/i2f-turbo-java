package i2f.extension.jackson.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ser.jdk.NumberSerializer;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonLong2StringSerializer extends ValueSerializer<Long> {
    public JacksonLong2StringSerializer() {
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext serializerProvider, BeanProperty beanProperty) {
        if (beanProperty == null) {
            return this;
        }
        if (beanProperty != null) {
            JsonFormat ann = beanProperty.getAnnotation(JsonFormat.class);
            if (ann != null) {
                return NumberSerializer.instance;
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
    public void serialize(Long obj, JsonGenerator jsonGenerator, SerializationContext serializerProvider) throws JacksonException {
        if (obj != null) {
            jsonGenerator.writeNumber(obj);
        } else {
            serializerProvider.defaultSerializeNullValue(jsonGenerator);
        }
    }

}
