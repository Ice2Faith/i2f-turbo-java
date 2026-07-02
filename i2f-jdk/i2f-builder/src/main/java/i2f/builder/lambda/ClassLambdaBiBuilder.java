package i2f.builder.lambda;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:15
 * @desc
 */
@FunctionalInterface
public interface ClassLambdaBiBuilder<T, R, V1, V2> {
    R set(T obj, V1 v1, V2 v2);
}

