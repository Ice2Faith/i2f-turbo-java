package i2f.extension.jackson.datetime.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonDateSerializer<T extends Date> extends ValueSerializer<T> {
    private DateFormat formatter;

    public JacksonDateSerializer(DateFormat formatter) {
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
                return new JacksonDateSerializer<>(new SimpleDateFormat(pattern));
            }
        }
        return this;
    }

    @Override
    public void serialize(T date, JsonGenerator jsonGenerator, SerializationContext serializerProvider) throws JacksonException {
        if (date != null) {
            try {
                String str = format(date, formatter);
                if (str != null) {
                    jsonGenerator.writeString(str);
                } else {
                    serializerProvider.writeValue(jsonGenerator, date);
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
