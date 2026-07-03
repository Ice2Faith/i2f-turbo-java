package i2f.builder.lambda;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2026/7/3 15:06
 * @desc
 */
@FunctionalInterface
public interface ObjectLambdaGetter<R> extends Serializable {
    R get();
}
