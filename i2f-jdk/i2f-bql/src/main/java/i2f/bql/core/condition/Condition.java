package i2f.bql.core.condition;

import i2f.bql.core.Bql;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/4 22:03
 * @desc
 */
@FunctionalInterface
public interface Condition {
    void apply(Bql<?> ret, String col);

    static Condition of(Condition cond) {
        return cond;
    }

    static Condition def(Object value) {
        if (value == null) {
            return $isNull();
        }
        if (value instanceof Collection) {
            return $in((Collection<? extends Object>) value);
        }
        if (value instanceof Iterable) {
            List<Object> list = new ArrayList<>();
            Iterable<?> iter = (Iterable<?>) value;
            for (Object o : iter) {
                list.add(o);
            }
            return $in(list);
        }
        if (value.getClass().isArray()) {
            List<Object> list = new ArrayList<>();
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                list.add(Array.get(value, i));
            }
            return $in(list);
        }
        return $eq(value);
    }

    static Condition $eq(Object value) {
        return of((q, c) -> q.$eq(c, value));
    }

    static Condition $like(Object value) {
        return of((q, c) -> q.$like(c, value));
    }

    static Condition $neq(Object value) {
        return of((q, c) -> q.$neq(c, value));
    }

    static Condition $ne(Object value) {
        return of((q, c) -> q.$ne(c, value));
    }

    static Condition $isNull() {
        return of(Bql::$isNull);
    }

    static Condition $isNotNull() {
        return of(Bql::$isNotNull);
    }

    static Condition $lt(Object value) {
        return of((q, c) -> q.$lt(c, value));
    }

    static Condition $lte(Object value) {
        return of((q, c) -> q.$lte(c, value));
    }

    static Condition $gt(Object value) {
        return of((q, c) -> q.$gt(c, value));
    }

    static Condition $gte(Object value) {
        return of((q, c) -> q.$gte(c, value));
    }

    static Condition $instr(Object value) {
        return of((q, c) -> q.$instr(c, value));
    }

    static <T> Condition $in(T... arr) {
        return of((q, c) -> q.$in(c, Arrays.asList(arr)));
    }

    static <T, C extends Collection<T>> Condition $in(C value) {
        return of((q, c) -> q.$in(c, value));
    }

    static <T> Condition $notIn(T... arr) {
        return of((q, c) -> q.$notIn(c, Arrays.asList(arr)));
    }

    static <T, C extends Collection<T>> Condition $notIn(C value) {
        return of((q, c) -> q.$notIn(c, value));
    }

    static Condition $exists(Supplier<Bql<?>> caller) {
        return of((q, c) -> q.$exists(caller));
    }

    static Condition $notExists(Supplier<Bql<?>> caller) {
        return of((q, c) -> q.$notExists(caller));
    }

    static Condition $and(Supplier<Bql<?>> caller) {
        return of((q, c) -> q.$and(caller));
    }

    static Condition $or(Supplier<Bql<?>> caller) {
        return of((q, c) -> q.$or(caller));
    }
}
