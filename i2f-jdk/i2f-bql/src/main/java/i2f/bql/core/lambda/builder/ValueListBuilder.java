package i2f.bql.core.lambda.builder;

import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/4 19:55
 * @desc
 */
public class ValueListBuilder implements Supplier<List<? extends Serializable>> {
    private List<Serializable> list = new ArrayList<>();

    public ValueListBuilder() {
    }

    public ValueListBuilder(List<? extends Serializable> list) {
        this.list.addAll(list);
    }

    public static ValueListBuilder create() {
        return new ValueListBuilder();
    }

    public static ValueListBuilder create(List<? extends Serializable> list) {
        return new ValueListBuilder(list);
    }

    @Override
    public List<? extends Serializable> get() {
        return list;
    }


    public ValueListBuilder addAll(List<? extends Serializable> list) {
        this.list.addAll(list);
        return this;
    }

    public ValueListBuilder apply(Consumer<List<? extends Serializable>> consumer) {
        consumer.accept(list);
        return this;
    }

    public ValueListBuilder apply(Function<List<? extends Serializable>, Collection<? extends Serializable>> function) {
        this.list = new ArrayList<>();
        this.list.addAll(function.apply(list));
        return this;
    }

    public ValueListBuilder add(Serializable value) {
        list.add(value);
        return this;
    }

    public <R, T> ValueListBuilder add(IGetter<R, T> getter) {
        list.add(getter);
        return this;
    }

    public <T, V> ValueListBuilder add(ISetter<T, V> setter) {
        list.add(setter);
        return this;
    }

    public <R, T, V> ValueListBuilder add(IBuilder<R, T, V> builder) {
        list.add(builder);
        return this;
    }

    public <T> ValueListBuilder add(IExecute<T> execute) {
        list.add(execute);
        return this;
    }

    public ValueListBuilder add(IFunctional func) {
        list.add(func);
        return this;
    }
}
