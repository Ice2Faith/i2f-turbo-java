package i2f.bql.core.value;

import i2f.bql.core.Bql;

import java.util.Arrays;
import java.util.Collection;
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
}
