package i2f.extension.jackson.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * @author Ice2Faith
 * @date 2023/11/27 9:49
 * @desc
 */
public abstract class BaseJacksonContextSerializer<T> extends JsonSerializer<T> implements ContextualSerializer {
    protected BeanProperty beanProperty;
    protected Class<?> fieldClass;
    protected JsonFormat fieldFormat;
    protected String formatPatten;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return this;
        }
        this.beanProperty = beanProperty;
        this.fieldClass = beanProperty.getType().getRawClass();
        this.fieldFormat = this.fieldClass.getAnnotation(JsonFormat.class);
        if (this.fieldFormat == null) {
            this.fieldFormat = this.fieldClass.getDeclaredAnnotation(JsonFormat.class);
        }
        if (this.fieldFormat != null) {
            this.formatPatten = this.fieldFormat.pattern();
        }
        return this;
    }

}
