package i2f.extension.jackson.datetime.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import i2f.convert.obj.ObjectConvertor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonLocalDateDeserializer extends JsonDeserializer<LocalDate> implements ContextualDeserializer {
    private DateTimeFormatter formatter;

    public JacksonLocalDateDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
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
                return new JacksonLocalDateDeserializer(DateTimeFormatter.ofPattern(pattern));
            }
        }
        return this;
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String str = jsonParser.getText();
        if (str == null) {
            return null;
        }

        try {
            return parse(str, formatter);
        } catch (Exception e) {
            if (e instanceof JsonProcessingException) {
                throw (JsonProcessingException) e;
            } else {
                throw new IOException(e.getMessage(), e);
            }
        }

    }

    public LocalDate parse(String str, DateTimeFormatter formatter) throws Exception {
        try {
            if (formatter != null) {
                return LocalDate.parse(str, formatter);
            }
        } catch (Exception e) {

        }
        return ObjectConvertor.parseLocalDate(str);
    }

}
