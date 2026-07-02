package i2f.builder.lambda;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:15
 * @desc
 */
@FunctionalInterface
public interface ObjectLambdaBuilder<R, E> {
    R set(E val);
}

