package i2f.extension.jackson.datetime.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonDateSerializer<T extends Date> extends JsonSerializer<T> implements ContextualSerializer {
    private DateFormat formatter;

    public JacksonDateSerializer(DateFormat formatter) {
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
                return new JacksonDateSerializer<>(new SimpleDateFormat(pattern));
            }
        }
        return this;
    }

    @Override
    public void serialize(T date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date != null) {
            try {
                String str = format(date, formatter);
                if (str != null) {
                    jsonGenerator.writeString(str);
                } else {
                    serializerProvider.defaultSerializeValue(date, jsonGenerator);
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

    public String format(T date, DateFormat formatter) throws Exception {
        try {
            if (formatter != null) {
                return formatter.format(date);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
