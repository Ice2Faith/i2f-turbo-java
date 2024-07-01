package i2f.log.perf;

/**
 * @author Ice2Faith
 * @date 2024/7/1 15:23
 * @desc 提供多参数
 */
@FunctionalInterface
public interface PerfSupplier<T> {
    T get(Object... args);
}
