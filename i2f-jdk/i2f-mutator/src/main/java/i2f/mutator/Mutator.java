package i2f.mutator;

import i2f.lambda.core.Lambda;
import i2f.lambda.core.func.IBuilder;
import i2f.lambda.core.func.ISetter;
import i2f.mutator.lambda.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:13
 * @desc 对象修改器，用于流式修改任意对象
 * 用法示例
 * 任意类实现接口
 * public class RestHttpResponse<T> implements BaseMutator<RestHttpResponse<T>> {
 * }
 * ----------------------------------
 * return new RestHttpResponse<T>().mutate() // 通过 BaseMutator 提供的默认方法转为 mutator
 * .set(u->u::setStatusCode,resp.getStatusCode()) // 通过实例引用进行设置值
 * .set(RestHttpResponse::setBody,obj) // 通过类引用进行设置值
 * .with(u->u::statusMessage,resp.getStatusMessage()) // with 适用于链式调用，返回源对象的情况
 * .with(RestHttpResponse::headers,resp.getHeader()) // with 适用于链式调用，返回源对象的情况
 * .set(u -> u::json) // 调用实体类的无参方法
 * .with(u->u::json) // 调用实体类的无参有返回值方法
 * .apply(HttpRequest::json) // 也可以通过类名方式调用无参方法
 * .done();
 */
public class Mutator<T> {
    protected T target;

    public Mutator(T target) {
        this.target = target;
    }

    public static <T> Mutator<T> of(T target) {
        return new Mutator<>(target);
    }

    public static <T> Mutator<T> of(Supplier<T> supplier) {
        return new Mutator<>(supplier.get());
    }

    /**
     * type 只是用来辅助IDE进行类型提示的，所以直接new一个匿名内部类即可
     * 例如：
     * <code>
     * of(ArrayList::new,String.class)
     * </code>
     * 这样得到的mutator类型就是以 String 进行补全提示的
     *
     * @param supplier
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Mutator<T> of(Supplier<T> supplier, Class<T> clazz) {
        return new Mutator<>(supplier.get());
    }

    /**
     * type 只是用来辅助IDE进行类型提示的，所以直接new一个匿名内部类即可
     * 例如：
     * <code>
     * of(HashMap::new,new MutatorType<Map<String,Object>>(){})
     * </code>
     * 这样得到的mutator类型就是以 Map<String,Object> 进行补全提示的
     *
     * @param supplier
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Mutator<T> of(Supplier<T> supplier, MutatorType<T> type) {
        return new Mutator<>(supplier.get());
    }

    public Mutator<T> apply(Consumer<T> consumer) {
        consumer.accept(target);
        return this;
    }

    public <R> Mutator<T> apply(Function<T, R> consumer) {
        consumer.apply(target);
        return this;
    }

    public Mutator<T> set(Function<T, Runnable> setter) {
        Runnable consumer = setter.apply(target);
        consumer.run();
        return this;
    }

    public <R> Mutator<T> with(Function<T, Supplier<R>> setter) {
        Supplier<R> consumer = setter.apply(target);
        consumer.get();
        return this;
    }

    public <E> Mutator<T> set(ISetter<T, E> setter, E val) {
        setter.accept(target, val);
        return this;
    }

    public <R, E> Mutator<T> with(IBuilder<R, T, E> setter, E val) {
        setter.apply(target, val);
        return this;
    }

    public <E> Mutator<T> set(Function<T, ObjectLambdaSetter<E>> setter, E val) {
        ObjectLambdaSetter<E> consumer = setter.apply(target);
        consumer.set(val);
        return this;
    }

    public <R, E> Mutator<T> with(Function<T, ObjectLambdaBuilder<R, E>> setter, E val) {
        ObjectLambdaBuilder<R, E> consumer = setter.apply(target);
        consumer.set(val);
        return this;
    }

    public <V1, V2> Mutator<T> set2(LambdaBiSetter<T, V1, V2> setter, V1 v1, V2 v2) {
        setter.set(target, v1, v2);
        return this;
    }

    public <R, V1, V2> Mutator<T> with2(LambdaBiBuilder<T, R, V1, V2> setter, V1 v1, V2 v2) {
        setter.set(target, v1, v2);
        return this;
    }

    public <V1, V2> Mutator<T> set2(Function<T, ObjectLambdaBiSetter<V1, V2>> setter, V1 v1, V2 v2) {
        ObjectLambdaBiSetter<V1, V2> consumer = setter.apply(target);
        consumer.set(v1, v2);
        return this;
    }

    public <R, V1, V2> Mutator<T> with2(Function<T, ObjectLambdaBiBuilder<R, V1, V2>> setter, V1 v1, V2 v2) {
        ObjectLambdaBiBuilder<R, V1, V2> consumer = setter.apply(target);
        consumer.set(v1, v2);
        return this;
    }

    public Mutator<T> when(Predicate<T> filter, Consumer<T> consumer) {
        if (filter == null || filter.test(target)) {
            consumer.accept(target);
        }
        return this;
    }


    public <R> Mutator<T> fieldNull(Function<T, Supplier<R>> filter, Consumer<T> consumer) {
        if (filter == null || filter.apply(target).get() == null) {
            consumer.accept(target);
        }
        return this;
    }

    public Mutator<T> fieldEmpty(Function<T, Supplier<?>> filter, Consumer<T> consumer) {
        if (filter == null || isEmpty(filter.apply(target).get())) {
            consumer.accept(target);
        }
        return this;
    }

    public Mutator<T> fieldBlank(Function<T, Supplier<String>> filter, Consumer<T> consumer) {
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


    public <R> Mutator<R> map(Function<T, R> mapper) {
        return of(mapper.apply(target));
    }

    @SuppressWarnings("unchecked")
    public <R extends T> Mutator<R> cast(Class<R> clazz) {
        return map(e -> (R) e);
    }

    public Mutator<T> orElse(T instead) {
        return orElse(Objects::isNull, instead);
    }

    public Mutator<T> orElse(Predicate<T> filter, T instead) {
        if (filter == null) {
            filter = Objects::isNull;
        }
        if (filter.test(target)) {
            target = instead;
        }
        return this;
    }

    public Mutator<T> orElse(Supplier<T> supplier) {
        return orElse(Objects::isNull, supplier);
    }

    public Mutator<T> orElse(Predicate<T> filter, Supplier<T> supplier) {
        if (filter == null) {
            filter = Objects::isNull;
        }
        if (filter.test(target)) {
            target = supplier.get();
        }
        return this;
    }

    public T done() {
        return target;
    }

    public <R> Mutator<T> fieldIfAbsent(Function<T, ObjectLambdaGetter<R>> getter, Supplier<R> supplier) {
        return fieldCompute(getter, v -> v == null ? supplier.get() : v);
    }

    public <R> Mutator<T> fieldIfAbsentV(Function<T, ObjectLambdaGetter<R>> getter, R val) {
        return fieldCompute(getter, v -> v == null ? val : v);
    }

    public <R> Mutator<T> fieldIfPresent(Function<T, ObjectLambdaGetter<R>> getter, Function<R, R> convertor) {
        return fieldCompute(getter, v -> v != null ? convertor.apply(v) : v);
    }

    public <R> Mutator<T> fieldCompute(Function<T, ObjectLambdaGetter<R>> getter, Function<R, R> computer) {
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

    @SuppressWarnings("unchecked")
    public <E> Mutator<T> fieldEachCompute(Class<E> fieldType, BiFunction<E, Field, E> computer) {
        return fieldEachCompute(field -> field.getType().isAssignableFrom(fieldType),
                (v, f) -> (E) computer.apply((E) v, f));
    }

    public Mutator<T> fieldEachCompute(Predicate<Field> fieldFilter, BiFunction<Object, Field, Object> computer) {
        Set<Field> fieldsSet = new LinkedHashSet<>();
        Lambda.walkField(target.getClass(), field -> {
            if (fieldFilter == null || fieldFilter.test(field)) {
                fieldsSet.add(field);
            }
            return true;
        });
        for (Field field : fieldsSet) {
            try {
                Method getterMethod = Lambda.getGetter(target.getClass(), field.getName());
                Object val = null;
                if (getterMethod != null) {
                    val = getterMethod.invoke(target);
                } else {
                    field.setAccessible(true);
                    val = field.get(target);
                }
                Object ret = computer.apply(val, field);
                // 这里直接比较引用，引用一致，则对象未发生变化，不进行赋值，因此不能使用 equals 比较
                if (ret != val) {
                    Method setterMethod = Lambda.getSetter(target.getClass(), field.getName());
                    if (setterMethod != null) {
                        setterMethod.invoke(target, ret);
                    } else {
                        field.setAccessible(true);
                        field.set(target, ret);
                    }
                }
            } catch (Exception e) {
                // ignore error
            }
        }
        return this;
    }

}
