package i2f.container.builder.obj;

import i2f.typeof.token.TypeToken;

import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/4/24 10:58
 * @desc
 */
public class ObjectBuilder<T> implements Supplier<T> {
    protected T obj;

    public ObjectBuilder(T obj) {
        this.obj = obj;
    }

    public ObjectBuilder(T obj, Class<T> type) {
        this.obj = obj;
    }

    public ObjectBuilder(T obj, TypeToken<T> type) {
        this.obj = obj;
    }

    @Override
    public T get() {
        return obj;
    }

    public <R> R getAs(Function<T, R> converter) {
        return converter.apply(obj);
    }

    public ObjectBuilder<T> then(Consumer<T> consumer) {
        consumer.accept(obj);
        return this;
    }

    public <U> ObjectBuilder<T> set(BiConsumer<T, U> consumer, U val) {
        consumer.accept(obj, val);
        return this;
    }

    public <U, R> ObjectBuilder<T> apply(BiFunction<T, U, R> function, U val) {
        function.apply(obj, val);
        return this;
    }

    public <R> ObjectBuilder<T> call(Function<T, R> function) {
        function.apply(obj);
        return this;
    }

}
