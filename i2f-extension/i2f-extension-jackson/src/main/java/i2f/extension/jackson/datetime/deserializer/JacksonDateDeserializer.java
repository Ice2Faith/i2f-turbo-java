package i2f.extension.jackson.datetime.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import i2f.convert.obj.ObjectConvertor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
@Data
@NoArgsConstructor
public class JacksonDateDeserializer extends ValueDeserializer<Date> {
    private DateFormat formatter;

    public JacksonDateDeserializer(DateFormat formatter) {
        this.formatter = formatter;
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
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
                return new JacksonDateDeserializer(new SimpleDateFormat(pattern));
            }
        }
        return this;
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        String str = jsonParser.getText();
        if (str == null) {
            return null;
        }

        try {
            return parse(str, formatter);
        } catch (Exception e) {
            if (e instanceof JacksonException) {
                throw (JacksonException) e;
            } else {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

    }

    public Date parse(String str, DateFormat formatter) throws Exception {
        try {
            if (formatter != null) {
                return formatter.parse(str);
            }
        } catch (Exception e) {

        }
        return ObjectConvertor.tryParseDate(str);
    }

}
