package i2f.lambda.core.func;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/3 16:48
 * @desc
 */
@FunctionalInterface
public interface IBuilder<R, T, V> extends Serializable {
    R apply(T obj, V val) throws Throwable;
}
