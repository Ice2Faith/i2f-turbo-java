package i2f.functional.adapt.except;

import i2f.functional.func.except.impl.IExFunction2;

/**
 * @author Ice2Faith
 * @date 2024/4/22 16:29
 * @desc
 */
@FunctionalInterface
public interface IExBuilder<R, T, V> extends IExFunction2<R, T, V> {
    static <R, T, V> IExBuilder<R, T, V> of(IExBuilder<R, T, V> ret) {
        return ret;
    }
}
