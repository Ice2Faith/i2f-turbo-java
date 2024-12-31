package i2f.streaming.type.str;

import i2f.streaming.impl.Reference;
import i2f.streaming.impl.StreamingImpl;
import i2f.streaming.impl.SupplierIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/6 16:28
 * @desc
 */
public class StringStreamingImpl<E> extends StreamingImpl<String> implements StringStreaming {
    private Function<E, String> stringifier;

    public StringStreamingImpl(Iterator<E> iterator, Function<E, String> stringifier) {
        super(stringIterator(iterator, stringifier));
        this.stringifier = stringifier;
    }

    public StringStreamingImpl(Iterator<E> iterator, Map<String, Object> globalContext, Function<E, String> stringifier) {
        super(stringIterator(iterator, stringifier), globalContext);
        this.stringifier = stringifier;
    }

    public StringStreamingImpl(Iterator<E> iterator, StreamingImpl<?> parent, Function<E, String> stringifier) {
        super(stringIterator(iterator, stringifier), parent);
        this.stringifier = stringifier;
    }

    public StringStreamingImpl(Iterator<String> iterator) {
        super(iterator);
    }

    public StringStreamingImpl(Iterator<String> iterator, Map<String, Object> globalContext) {
        super(iterator, globalContext);
    }

    public StringStreamingImpl(Iterator<String> iterator, StreamingImpl<?> parent) {
        super(iterator, parent);
    }

    public static <E> Iterator<String> stringIterator(Iterator<E> iterator, Function<E, String> stringifier) {
        return new SupplierIterator<>(() -> {
            while (iterator.hasNext()) {
                E elem = iterator.next();
                String str = null;
                if (stringifier != null) {
                    str = stringifier.apply(elem);
                } else {
                    if (elem != null) {
                        if (elem instanceof String) {
                            str = (String) elem;
                        } else {
                            str = String.valueOf(elem);
                        }
                    }
                }
                return Reference.of(str);
            }
            return Reference.finish();
        });
    }

}
