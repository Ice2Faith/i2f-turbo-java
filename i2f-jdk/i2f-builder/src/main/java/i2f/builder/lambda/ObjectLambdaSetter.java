package i2f.builder.lambda;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:14
 * @desc
 */
@FunctionalInterface
public interface ObjectLambdaSetter<E> extends Serializable {
    void set(E val);
}
