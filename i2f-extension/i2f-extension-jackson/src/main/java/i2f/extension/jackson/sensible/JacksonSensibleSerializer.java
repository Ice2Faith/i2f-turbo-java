package i2f.extension.jackson.sensible;

import i2f.extension.jackson.sensible.annotations.Sensible;
import i2f.extension.jackson.sensible.handler.ISensibleHandler;
import i2f.extension.jackson.sensible.handler.impl.TruncateSensibleHandler;
import i2f.extension.jackson.sensible.holder.SensibleHandlersHolder;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.Collection;
import java.util.Set;

public class JacksonSensibleSerializer extends ValueSerializer<Object> {

    private ISensibleHandler handler;
    private Sensible ann;

    @Override
    public void serialize(Object obj, JsonGenerator jsonGenerator, SerializationContext serializerProvider) throws JacksonException {
        obj = handler.handle(obj, ann);
        serializerProvider.writeValue(jsonGenerator, obj);
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext serializerProvider, BeanProperty beanProperty) {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        Sensible ann = beanProperty.getAnnotation(Sensible.class);
        if (ann == null) {
            ann = beanProperty.getContextAnnotation(Sensible.class);
        }
        if (ann == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType());
        }
        Class<?> rawClass = beanProperty.getType().getRawClass();
        Collection<ISensibleHandler> handlers = SensibleHandlersHolder.getContextHandlers();
        if (handlers != null) {
            for (ISensibleHandler handler : handlers) {
                Set<String> type = handler.accept();
                if (type.contains(ann.type())) {
                    Set<Class<?>> types = handler.type();
                    for (Class<?> tp : types) {
                        if (tp.isAssignableFrom(rawClass)) {
                            JacksonSensibleSerializer serializer = new JacksonSensibleSerializer();
                            serializer.handler = handler;
                            serializer.ann = ann;
                            return serializer;
                        }
                    }
                }
            }
        }
        JacksonSensibleSerializer serializer = new JacksonSensibleSerializer();
        serializer.handler = new TruncateSensibleHandler();
        serializer.ann = ann;
        return serializer;
    }


}
