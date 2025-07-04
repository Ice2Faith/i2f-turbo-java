package i2f.reflect.vistor;

import i2f.reflect.vistor.impl.VisitorParser;
import i2f.typeof.token.TypeToken;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:00
 * @desc
 */
public interface Visitor {
    Object get();

    default <T> T castAs() {
        return (T) get();
    }

    default <T> T castAs(Class<T> clazz) {
        return (T) get();
    }

    default <T> T castAs(TypeToken<T> token) {
        return (T) get();
    }

    default <T> T getAs(Function<Object, T> mapper) {
        return mapper.apply(get());
    }

    default <T> T getAs(BiFunction<Object, T, T> mapper, T defVal) {
        return mapper.apply(get(), defVal);
    }

    void set(Object value);

    default void delete() {
        throw new IllegalStateException("un-support delete operation on type" + getClass());
    }

    default Object parent() {
        throw new IllegalStateException("not parent support");
    }

    static Visitor visit(String expression, Object rootObj) {
        return VisitorParser.visit(expression, rootObj);
    }

    static Visitor visit(String expression, Object rootObj, Object paramObj) {
        return VisitorParser.visit(expression, rootObj, paramObj);
    }
}
