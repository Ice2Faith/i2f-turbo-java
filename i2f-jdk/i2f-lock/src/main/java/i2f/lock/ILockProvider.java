package i2f.lock;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:11
 */
public interface ILockProvider {
    String name();

    ILock getLock(String key);
}
