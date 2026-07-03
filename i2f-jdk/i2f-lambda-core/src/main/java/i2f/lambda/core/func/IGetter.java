package i2f.lambda.core.func;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/3 16:47
 * @desc
 */
@FunctionalInterface
public interface IGetter<R, V> extends Serializable {
    R apply(V v) throws Throwable;
}
