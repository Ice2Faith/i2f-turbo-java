package i2f.limit;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2026/9/22 14:39
 * @desc 按键区分的限流器
 */
public interface IKeyedLimiter {
    ILimiter get(String key);

    ILimiter get(String key, Function<String, ILimiter> producer);

    ILimiter remove(String key);
}
