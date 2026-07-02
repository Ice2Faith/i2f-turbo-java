package i2f.builder;

import i2f.builder.lambda.*;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:13
 * @desc 对象构建器，用于流式构建任意对象
 * 用法示例
 * 任意类实现接口
 * public class RestHttpResponse<T> implements BaseBuilder<RestHttpResponse<T>> {
 * }
 * ----------------------------------
 * return new RestHttpResponse<T>().toBuilder() // 通过 BaseBuilder 提供的默认方法转为 builder
 * .set(u->u::setStatusCode,resp.getStatusCode()) // 通过实例引用进行设置值
 * .set(RestHttpResponse::setBody,obj) // 通过类引用进行设置值
 * .with(u->u::statusMessage,resp.getStatusMessage()) // with 适用于链式调用，返回源对象的情况
 * .with(RestHttpResponse::headers,resp.getHeader()) // with 适用于链式调用，返回源对象的情况
 * .build();
 */
public class Builder<T> {
    protected T target;

    public Builder(T target) {
        this.target = target;
    }

    public static <T> Builder<T> of(T target) {
        return new Builder<>(target);
    }

    public static <T> Builder<T> of(Supplier<T> supplier) {
        return new Builder<>(supplier.get());
    }

    public static <T> Builder<T> of(Supplier<T> supplier, Class<T> clazz) {
        return new Builder<>(supplier.get());
    }

    public static <T> Builder<T> of(Supplier<T> supplier, BuilderType<T> type) {
        return new Builder<>(supplier.get());
    }

    public Builder<T> apply(Consumer<T> consumer) {
        consumer.accept(target);
        return this;
    }

    public <E> Builder<T> set(ClassLambdaSetter<T, E> setter, E val) {
        setter.set(target, val);
        return this;
    }

    public <R, E> Builder<T> with(ClassLambdaBuilder<T, R, E> setter, E val) {
        setter.set(target, val);
        return this;
    }

    public <E> Builder<T> set(Function<T, ObjectLambdaSetter<E>> setter, E val) {
        ObjectLambdaSetter<E> consumer = setter.apply(target);
        consumer.set(val);
        return this;
    }

    public <R, E> Builder<T> with(Function<T, ObjectLambdaBuilder<R, E>> setter, E val) {
        ObjectLambdaBuilder<R, E> consumer = setter.apply(target);
        consumer.set(val);
        return this;
    }

    public <V1, V2> Builder<T> set2(ClassLambdaBiSetter<T, V1, V2> setter, V1 v1, V2 v2) {
        setter.set(target, v1, v2);
        return this;
    }

    public <R, V1, V2> Builder<T> with2(ClassLambdaBiBuilder<T, R, V1, V2> setter, V1 v1, V2 v2) {
        setter.set(target, v1, v2);
        return this;
    }

    public <V1, V2> Builder<T> set2(Function<T, ObjectLambdaBiSetter<V1, V2>> setter, V1 v1, V2 v2) {
        ObjectLambdaBiSetter<V1, V2> consumer = setter.apply(target);
        consumer.set(v1, v2);
        return this;
    }

    public <R, V1, V2> Builder<T> with2(Function<T, ObjectLambdaBiBuilder<R, V1, V2>> setter, V1 v1, V2 v2) {
        ObjectLambdaBiBuilder<R, V1, V2> consumer = setter.apply(target);
        consumer.set(v1, v2);
        return this;
    }

    public Builder<T> when(Predicate<T> filter, Consumer<T> consumer) {
        if (filter == null || filter.test(target)) {
            consumer.accept(target);
        }
        return this;
    }

    public <R> Builder<R> map(Function<T, R> mapper) {
        return of(mapper.apply(target));
    }

    public Builder<T> orElse(T instead) {
        return orElse(Objects::isNull, instead);
    }

    public Builder<T> orElse(Predicate<T> filter, T instead) {
        if (filter == null) {
            filter = Objects::isNull;
        }
        if (filter.test(target)) {
            target = instead;
        }
        return this;
    }

    public Builder<T> orElse(Supplier<T> supplier) {
        return orElse(Objects::isNull, supplier);
    }

    public Builder<T> orElse(Predicate<T> filter, Supplier<T> supplier) {
        if (filter == null) {
            filter = Objects::isNull;
        }
        if (filter.test(target)) {
            target = supplier.get();
        }
        return this;
    }

    public T build() {
        return target;
    }
}
