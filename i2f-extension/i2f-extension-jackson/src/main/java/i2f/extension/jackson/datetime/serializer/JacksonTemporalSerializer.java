package i2f.extension.jackson.datetime.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.JsonMappingException;
import tools.jackson.databind.JsonSerializer;
import tools.jackson.databind.SerializerProvider;
import tools.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonTemporalSerializer<T extends Temporal> extends JsonSerializer<T> implements ContextualSerializer {
    private DateTimeFormatter formatter;

    public JacksonTemporalSerializer() {
    }

    public JacksonTemporalSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
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
    public void serialize(T temporal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (temporal != null) {
            try {
                String str = format(temporal, formatter);
                if (str != null) {
                    jsonGenerator.writeString(str);
                } else {
                    serializerProvider.defaultSerializeValue(temporal, jsonGenerator);
                }
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new IOException(e.getMessage(), e);
                }
            }
        } else {
            serializerProvider.defaultSerializeNull(jsonGenerator);
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
