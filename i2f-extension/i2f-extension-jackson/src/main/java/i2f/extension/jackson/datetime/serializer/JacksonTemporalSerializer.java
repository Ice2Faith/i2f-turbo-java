package i2f.extension.jackson.datetime.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonTemporalSerializer<T extends Temporal> extends ValueSerializer<T> {
    private DateTimeFormatter formatter;

    public JacksonTemporalSerializer() {
    }

    public JacksonTemporalSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext serializerProvider, BeanProperty beanProperty) {
        if (beanProperty == null) {
            return this;
        }
        JsonFormat ann = beanProperty.getAnnotation(JsonFormat.class);
        if (ann == null) {
            ann = beanProperty.getAnnotation(JsonFormat.class);
        }
        if (ann != null) {
            String pattern = ann.pattern();
            if (pattern != null && !pattern.isEmpty()) {
                return new JacksonTemporalSerializer<>(DateTimeFormatter.ofPattern(pattern));
            }
        }
        return this;
    }

    @Override
    public void serialize(T temporal, JsonGenerator jsonGenerator, SerializationContext serializerProvider) throws JacksonException {
        if (temporal != null) {
            try {
                String str = format(temporal, formatter);
                if (str != null) {
                    jsonGenerator.writeString(str);
                } else {
                    serializerProvider.writeValue(jsonGenerator, temporal);
                }
            } catch (Exception e) {
                if (e instanceof JacksonException) {
                    throw (JacksonException) e;
                } else {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
        } else {
            serializerProvider.defaultSerializeNullValue(jsonGenerator);
        }
    }

    public String format(T temporal, DateTimeFormatter formatter) throws Exception {
        try {
            if (formatter != null) {
                return formatter.format(temporal);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
