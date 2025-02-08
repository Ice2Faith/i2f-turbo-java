package i2f.lock;

/**
 * @author Ice2Faith
 * @date 2022/4/15 8:36
 * @desc
 */
public interface ILock {
    void lock() throws Throwable;

    void unlock() throws Throwable;
}
