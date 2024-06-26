package i2f.extension.jackson.sensible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import i2f.extension.jackson.sensible.annotations.Sensible;
import i2f.extension.jackson.sensible.handler.ISensibleHandler;
import i2f.extension.jackson.sensible.handler.impl.TruncateSensibleHandler;
import i2f.extension.jackson.sensible.holder.SensibleHandlersHolder;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class JacksonSensibleSerializer extends JsonSerializer<Object>
        implements ContextualSerializer {

    private ISensibleHandler handler;
    private Sensible ann;

    @Override
    public void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        obj = handler.handle(obj, ann);
        jsonGenerator.writeObject(obj);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        Sensible ann = beanProperty.getAnnotation(Sensible.class);
        if (ann == null) {
            ann = beanProperty.getContextAnnotation(Sensible.class);
        }
        if (ann == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        Class<?> rawClass = beanProperty.getType().getRawClass();
        Collection<ISensibleHandler> handlers = SensibleHandlersHolder.getContextHandlers();
        if(handlers!=null){
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
