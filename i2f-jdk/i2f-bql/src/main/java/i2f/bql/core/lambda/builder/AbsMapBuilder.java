package i2f.bql.core.lambda.builder;

import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/4 19:55
 * @desc
 */
public class AbsMapBuilder<E> implements Supplier<Map<? extends Serializable, E>> {
    protected Map<Serializable, E> map = new LinkedHashMap<>();

    public AbsMapBuilder() {
    }

    public AbsMapBuilder(Map<? extends Serializable, E> map) {
        this.map.putAll(map);
    }

    @Override
    public Map<? extends Serializable, E> get() {
        return map;
    }

    public AbsMapBuilder<E> putAll(Map<? extends Serializable, E> map) {
        this.map.putAll(map);
        return this;
    }

    public AbsMapBuilder<E> apply(Consumer<Map<? extends Serializable, E>> consumer) {
        consumer.accept(map);
        return this;
    }

    public AbsMapBuilder<E> apply(Function<Map<? extends Serializable, E>, Map<? extends Serializable, E>> function) {
        this.map = new LinkedHashMap<>();
        this.map.putAll(function.apply(map));
        return this;
    }

    public AbsMapBuilder<E> put(Serializable key, E value) {
        map.put(key, value);
        return this;
    }

    public <R, T> AbsMapBuilder<E> put(IGetter<R, T> getter, E value) {
        map.put(getter, value);
        return this;
    }

    public <T, V> AbsMapBuilder<E> put(ISetter<T, V> setter, E value) {
        map.put(setter, value);
        return this;
    }

    public <R, T, V> AbsMapBuilder<E> put(IBuilder<R, T, V> builder, E value) {
        map.put(builder, value);
        return this;
    }

    public <T> AbsMapBuilder<E> put(IExecute<T> execute, E value) {
        map.put(execute, value);
        return this;
    }

    public AbsMapBuilder<E> put(IFunctional func, E value) {
        map.put(func, value);
        return this;
    }
}
