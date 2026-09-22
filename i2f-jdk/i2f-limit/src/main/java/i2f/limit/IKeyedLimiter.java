package i2f.limit;

/**
 * @author Ice2Faith
 * @date 2026/9/22 14:39
 * @desc 按键区分的限流器
 */
public interface IKeyedLimiter {
    ILimiter get(String key);

    ILimiter remove(String key);
}
