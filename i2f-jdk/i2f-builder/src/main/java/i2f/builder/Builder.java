package i2f.builder;

import i2f.builder.lambda.*;
import i2f.lambda.core.Lambda;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
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
 * .set(u -> u::json) // 调用实体类的无参方法
 * .with(u->u::json) // 调用实体类的无参有返回值方法
 * .apply(HttpRequest::json) // 也可以通过类名方式调用无参方法
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

    /**
     * type 只是用来辅助IDE进行类型提示的，所以直接new一个匿名内部类即可
     * 例如：
     * <code>
     * of(ArrayList::new,String.class)
     * </code>
     * 这样得到的builder类型就是以 String 进行补全提示的
     *
     * @param supplier
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Builder<T> of(Supplier<T> supplier, Class<T> clazz) {
        return new Builder<>(supplier.get());
    }

    /**
     * type 只是用来辅助IDE进行类型提示的，所以直接new一个匿名内部类即可
     * 例如：
     * <code>
     * of(HashMap::new,new BuilderType<Map<String,Object>>(){})
     * </code>
     * 这样得到的builder类型就是以 Map<String,Object> 进行补全提示的
     *
     * @param supplier
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Builder<T> of(Supplier<T> supplier, BuilderType<T> type) {
        return new Builder<>(supplier.get());
    }

    public Builder<T> apply(Consumer<T> consumer) {
        consumer.accept(target);
        return this;
    }

    public <R> Builder<T> apply(Function<T, R> consumer) {
        consumer.apply(target);
        return this;
    }

    public Builder<T> set(Function<T, Runnable> setter) {
        Runnable consumer = setter.apply(target);
        consumer.run();
        return this;
    }

    public <R> Builder<T> with(Function<T, Supplier<R>> setter) {
        Supplier<R> consumer = setter.apply(target);
        consumer.get();
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


    public <R> Builder<T> fieldNull(Function<T, Supplier<R>> filter, Consumer<T> consumer) {
        if (filter == null || filter.apply(target).get() == null) {
            consumer.accept(target);
        }
        return this;
    }

    public Builder<T> fieldEmpty(Function<T, Supplier<?>> filter, Consumer<T> consumer) {
        if (filter == null || isEmpty(filter.apply(target).get())) {
            consumer.accept(target);
        }
        return this;
    }

    public Builder<T> fieldBlank(Function<T, Supplier<String>> filter, Consumer<T> consumer) {
        if (filter == null || isBlank(filter.apply(target).get())) {
            consumer.accept(target);
        }
        return this;
    }

    protected static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }


    protected boolean isBlank(String str) {
        if (isEmpty(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        } else if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            return col.isEmpty();
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            CharSequence sequence = (CharSequence) obj;
            return sequence.length() == 0;
        }
        return false;
    }


    public <R> Builder<R> map(Function<T, R> mapper) {
        return of(mapper.apply(target));
    }

    @SuppressWarnings("unchecked")
    public <R extends T> Builder<R> cast(Class<R> clazz) {
        return map(e -> (R) e);
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

    public <R> Builder<T> fieldIfAbsent(Function<T, ObjectLambdaGetter<R>> getter, Supplier<R> supplier) {
        return fieldCompute(getter, v -> v == null ? supplier.get() : v);
    }

    public <R> Builder<T> fieldIfAbsentV(Function<T, ObjectLambdaGetter<R>> getter, R val) {
        return fieldCompute(getter, v -> v == null ? val : v);
    }

    public <R> Builder<T> fieldCompute(Function<T, ObjectLambdaGetter<R>> getter, Function<R, R> computer) {
        try {
            ObjectLambdaGetter<R> lambdaGetter = getter.apply(target);
            R val = lambdaGetter.get();
            R ret = computer.apply(val);
            // 这里直接比较引用，引用一致，则对象未发生变化，不进行赋值，因此不能使用 equals 比较
            if (ret != val) {
                Method setter = Lambda.ofSetter(target.getClass(), lambdaGetter);
                if (setter == null) {
                    String fieldName = Lambda.ofFieldName(lambdaGetter);
                    throw new IllegalStateException("cannot find setter for field : " + fieldName);
                }
                setter.invoke(target, ret);
            }
            return this;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
