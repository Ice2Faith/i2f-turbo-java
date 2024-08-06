package i2f.limit;

/**
 * @author Ice2Faith
 * @date 2024/8/6 11:05
 * @desc
 */
public interface Limiter {
    boolean require(String name);
}
