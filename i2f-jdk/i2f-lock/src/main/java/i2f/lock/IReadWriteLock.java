package i2f.lock;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:12
 */
public interface IReadWriteLock {
    ILock readLock();

    ILock writeLock();
}
