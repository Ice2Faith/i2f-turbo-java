package i2f.mutator.lambda;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:15
 * @desc
 */
@FunctionalInterface
public interface LambdaBiSetter<T, V1, V2> extends Serializable {
    void set(T obj, V1 v1, V2 v2);
}

