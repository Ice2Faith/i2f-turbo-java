package i2f.mutator.lambda;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:15
 * @desc
 */
@FunctionalInterface
public interface ObjectLambdaBuilder<R, E> extends Serializable {
    R set(E val);
}

