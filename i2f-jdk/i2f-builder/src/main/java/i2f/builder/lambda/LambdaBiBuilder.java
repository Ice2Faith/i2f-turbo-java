package i2f.builder.lambda;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:15
 * @desc
 */
@FunctionalInterface
public interface LambdaBiBuilder<T, R, V1, V2> extends Serializable {
    R set(T obj, V1 v1, V2 v2);
}

