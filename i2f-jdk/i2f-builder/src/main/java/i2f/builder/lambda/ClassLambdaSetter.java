package i2f.builder.lambda;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:14
 * @desc
 */
@FunctionalInterface
public interface ClassLambdaSetter<T, E> {
    void set(T obj, E val);
}
